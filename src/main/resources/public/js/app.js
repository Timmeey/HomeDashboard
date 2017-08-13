"use strict";
function Clock(dateTime, syncInterval) {
    this.date = dateTime;
    this.syncInterval = syncInterval;
    var tis = this;
    setInterval(function () {
        var newDate = new Date();
        newDate.setSeconds(tis.date.getSeconds() + 1);
        tis.date = newDate;
    }, 999);
    setInterval(function () {
        tis.retrieveTime()
    }, this.syncInterval);


    this.retrieveTime = function () {
        axios.get("/date").then(function (response) {
            tis.date = new Date(Date.parse(response.data.datetime.split("[")[0]));
        });
    }
}

var app = new Vue({
    el: "#app",
    data: {
        dateTime: new Clock(new Date(), 60000),
        station: ''
    },
    methods: {
        updateStation: function () {
            var tis = this;
            axios.get("/oeffi").then(function (response) {
                var lines = [];
                for (var i in response.data.lines) {
                    var tmp = response.data.lines[i];
                    lines.push(new Line(tmp.departureIn,
                        tmp.lineName,
                        tmp.destination,
                        tmp.type)
                    )
                }
                var station = new Station(response.data.name, lines);
                tis.station = station;
            })
        }

    },
    computed: {
        filteredLines: function () {
            var result = [];
            for (var i in this.station.lines) {
                if (this.station.lines[i].type === "Tram" && this.station.lines[i].departureIn > 0) {
                    result.push(this.station.lines[i])
                }
            }
            return result;

        }
    }
});


//setInterval(function () {
//   app.updateStation()
//}, 10000);


class Station {
    constructor(name, lines) {
        this.name = name;
        this.lines = lines;
    }
}

class Line {
    constructor(departure, name, dest, type) {
        this.departureIn = departure;
        this.name = name;
        this.dest = dest;
        this.type = type;
    };
}
Line.prototype.print = function () {
    var res = this.name + '=> ' + this.dest + ': ';
    if (this.departureIn < 2) {
        res = res + "NOW";
    } else {
        res = res + this.departureIn;
    }

    return res;
};



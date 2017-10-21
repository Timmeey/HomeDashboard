package de.timmeey.iot.homeDashboard.util.httptuils;

import java.util.List;
import java.util.Optional;
import lombok.val;
import ro.pippo.core.Request;

/**
 * EnhancedRequest.
 * @author Tim Hinkes (timmeey@timmeey.de)
 * @version $Id:\$
 * @since 0.1
 */
public class EnhancedRequest {

    private final Request src;

    public EnhancedRequest(final Request src) {
        this.src = src;
    }

    public Optional<Integer> getQueryParamAsInt(String name){
        val param = src.getQueryParameter(name);
        if (param.isMultiValued() || param.isEmpty() || param.isNull()){
            return Optional.empty();
        }else{
            try{
                return Optional.of(param.toInt());
            }catch (Exception e){
                return Optional.empty();
            }
        }

    }
    public Optional<String> getQueryParamAsString(String name){
        val param = src.getQueryParameter(name);
        if (param.isMultiValued() || param.isEmpty() || param.isNull()){
            return Optional.empty();
        }else{
            try{
                return Optional.of(param.toString());
            }catch (Exception e){
                return Optional.empty();
            }
        }
    }
    public List<String> getQueryParamAsList(String name){
        return src.getQueryParameter(name).toList();
    }
}

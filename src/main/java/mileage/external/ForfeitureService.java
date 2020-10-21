package mileage.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="forfeiture", url="${api.point.url}")
public interface ForfeitureService {
    @RequestMapping(method= RequestMethod.POST, path="/forfeitures")
    public void forfeitHstInsert(@RequestBody Forfeiture forfeiture);
}

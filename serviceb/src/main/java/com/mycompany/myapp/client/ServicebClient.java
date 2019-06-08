package com.mycompany.myapp.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mycompany.myapp.domain.Entityb;

import java.util.List;

@AuthorizedFeignClient(name = "serviceb")
public interface ServicebClient {

    @RequestMapping(path = "/api/entitybs", method = RequestMethod.POST)
    public ResponseEntity<Entityb> createEntityb(@RequestBody Entityb entityb);

    @RequestMapping(path = "/api/entitybs", method = RequestMethod.PUT)
    public ResponseEntity<Entityb> updateEntityb(@RequestBody Entityb entityb);

    @RequestMapping(path = "/api/entitybs", method = RequestMethod.GET)
    public List<Entityb> getAllEntitybs();

    @RequestMapping(path = "/api/entitybs/{id}", method = RequestMethod.GET)
    public ResponseEntity<Entityb> getEntityb(@PathVariable Long id);

    @RequestMapping(path = "/api/entitybs/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteEntityb(@PathVariable Long id);

}

package tr.gov.tmo.erp.kantarsahaservice.controller;

import org.springframework.web.bind.annotation.*;
import tr.gov.tmo.erp.kantarsahaservice.model.KantarTestRequest;
import tr.gov.tmo.erp.kantarsahaservice.model.PortScanResult;
import tr.gov.tmo.erp.kantarsahaservice.service.KantarService;

import java.util.List;

@RestController
@RequestMapping("kantar")
@CrossOrigin("*")
public class KantarController {

    private final KantarService kantarService;

    public KantarController(KantarService kantarService) {
        this.kantarService = kantarService;
    }

    @PostMapping
    public PortScanResult test(@RequestBody KantarTestRequest body) {
        PortScanResult check = kantarService.check(body);
        return check;
    }

    @GetMapping("ports")
    List<String> comList() {
        return  kantarService.comList();
    }


}

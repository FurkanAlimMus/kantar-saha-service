package tr.gov.tmo.erp.kantarsahaservice.service;

import tr.gov.tmo.erp.kantarsahaservice.model.KantarTestRequest;
import tr.gov.tmo.erp.kantarsahaservice.model.PortScanResult;

import java.util.List;

public interface KantarService {
    PortScanResult check(KantarTestRequest body);

    List<String> comList();
}

package tr.gov.tmo.erp.kantarsahaservice.service;

import tr.gov.tmo.erp.kantarsahaservice.model.KantarTestRequest;

import java.util.List;

public interface KantarService {
    boolean check(KantarTestRequest body);

    List<String> comList();

    int kantarOku();
}

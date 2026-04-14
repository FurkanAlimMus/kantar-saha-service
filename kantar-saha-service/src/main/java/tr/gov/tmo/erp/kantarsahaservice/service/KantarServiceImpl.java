package tr.gov.tmo.erp.kantarsahaservice.service;
import org.springframework.stereotype.Service;
import com.fazecast.jSerialComm.SerialPort;
import tr.gov.tmo.erp.kantarsahaservice.business.FileProses;
import tr.gov.tmo.erp.kantarsahaservice.model.KantarTestRequest;
import tr.gov.tmo.erp.kantarsahaservice.model.PortScanResult;

import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KantarServiceImpl implements KantarService {




    @Override
    public boolean check(KantarTestRequest body) {
        return FileProses.writeKantarInfo(body);
    }

    @Override
    public int kantarOku() {

        return 0;
    }

    @Override
    public List<String> comList() {

        return Arrays.stream(SerialPort.getCommPorts()).map(SerialPort::getSystemPortName).collect(Collectors.toList());


    }



}

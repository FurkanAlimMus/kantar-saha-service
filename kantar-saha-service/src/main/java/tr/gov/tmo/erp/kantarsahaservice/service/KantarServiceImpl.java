package tr.gov.tmo.erp.kantarsahaservice.service;
import org.springframework.stereotype.Service;
import com.fazecast.jSerialComm.SerialPort;
import tr.gov.tmo.erp.kantarsahaservice.business.FileProses;
import tr.gov.tmo.erp.kantarsahaservice.model.KantarTestRequest;

import java.util.Arrays;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KantarServiceImpl implements KantarService {



    @Override
    public boolean check(KantarTestRequest body) {
        return FileProses.writeKantarInfo(body);
//       return PortScanner.scannerPort(body.port(), body.kilo());

    }

    @Override
    public List<String> comList() {

        return Arrays.stream(SerialPort.getCommPorts()).map(SerialPort::getSystemPortName).collect(Collectors.toList());


    }

}

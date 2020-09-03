package xyz.flysium.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.flysium.dao.entity.TableC;
import xyz.flysium.dao.repository.TableCMapper;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@RestController
public class CTestController {

    @Autowired
    private TableCMapper tableCMapper;

    @GetMapping("/rm_c")
    public String rm_c() {
        final TableC record = new TableC();
        record.setName("rm3");
        tableCMapper.insert(record);
        return "success";
    }

}

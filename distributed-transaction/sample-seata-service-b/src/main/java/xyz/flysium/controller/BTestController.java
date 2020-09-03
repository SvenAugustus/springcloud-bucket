package xyz.flysium.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.flysium.dao.entity.TableB;
import xyz.flysium.dao.repository.TableBMapper;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@RestController
public class BTestController {

    @Autowired
    private TableBMapper tableBMapper;

    @GetMapping("/rm_b")
    public String rm_b() {
        final TableB record = new TableB();
        record.setName("rm2");
        tableBMapper.insert(record);
        return "success";
    }

}

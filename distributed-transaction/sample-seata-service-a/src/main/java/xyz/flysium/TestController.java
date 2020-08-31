package xyz.flysium;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.flysium.entity.TableA;
import xyz.flysium.repository.TableAMapper;

/**
 * @author zeno (Sven Augustus)
 * @version 1.0
 */
@RestController
public class TestController {

    @Autowired
    private TableAMapper tableAMapper;

    @Autowired
    private ServiceBClient serviceBClient;

    @Autowired
    private ServiceCClient serviceCClient;

    /**
     * 测试全局事务的提交
     */
    @GetMapping("/rm_test")
    @GlobalTransactional(rollbackFor = Exception.class)
    public String rm_test() {
        serviceCClient.rmInvokerC();
        serviceBClient.rmInvokerB();

        rm_a();
        return "success";
    }

    /**
     * 测试全局事务的回滚
     */
    @GetMapping("/rm_rollback")
    @GlobalTransactional(rollbackFor = Exception.class)
    public String rm_rollback() {
        serviceCClient.rmInvokerC();
        serviceBClient.rmInvokerB();

        rm_a();

        int i = 1 / 0;
        System.out.println("Error~~");
        return "success";
    }

    public String rm_a() {
        final TableA record = new TableA();
        record.setName("rm1");
        tableAMapper.insert(record);
        return "success";
    }

}

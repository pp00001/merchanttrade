package ins.platform.aggpay.admin.controller;

import java.util.Map;

import ins.platform.aggpay.admin.model.entity.SysOauthClientDetails;
import ins.platform.aggpay.common.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import ins.platform.aggpay.common.util.Query;
import ins.platform.aggpay.admin.service.SysOauthClientDetailsService;
import ins.platform.aggpay.common.web.BaseController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2018-05-15
 */
@RestController
@RequestMapping("/client")
public class OauthClientDetailsController extends BaseController {
    @Autowired
    private SysOauthClientDetailsService sysOauthClientDetailsService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return SysOauthClientDetails
     */
    @GetMapping("/{id}")
    public SysOauthClientDetails get(@PathVariable Integer id) {
        return sysOauthClientDetailsService.selectById(id);
    }


    /**
     * 分页查询信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @RequestMapping("/page")
    public Page page(@RequestParam Map<String, Object> params) {
        return sysOauthClientDetailsService.selectPage(new Query<>(params), new EntityWrapper<>());
    }

    /**
     * 添加
     *
     * @param sysOauthClientDetails 实体
     * @return success/false
     */
    @PostMapping
    public R<Boolean> add(@RequestBody SysOauthClientDetails sysOauthClientDetails) {
        return new R<>(sysOauthClientDetailsService.insert(sysOauthClientDetails));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        SysOauthClientDetails sysOauthClientDetails = new SysOauthClientDetails();
        sysOauthClientDetails.setClientId(id);
        return new R<>(sysOauthClientDetailsService.deleteById(sysOauthClientDetails));
    }

    /**
     * 编辑
     *
     * @param sysOauthClientDetails 实体
     * @return success/false
     */
    @PutMapping
    public R<Boolean> edit(@RequestBody SysOauthClientDetails sysOauthClientDetails) {
        return new R<>(sysOauthClientDetailsService.updateById(sysOauthClientDetails));
    }
}

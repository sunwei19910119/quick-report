package ${package.Controller};
import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
import com.xhtt.common.utils.R;
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
    * ${table.comment!} 前端控制器
    * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@Api(tags = "${table.comment}列表",value = "${table.comment}列表")
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
    <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
    <#else>
public class ${table.controllerName} {
    </#if>

       @Autowired
        private ${table.serviceName} ${table.entityPath}Service;

        /**
         * 获取${table.comment}列表
         */
        @GetMapping(value = "/list")
        @ApiOperation("获取${table.comment}列表")
        @Login
        public R list(@RequestParam Map<String,Object> params) {
            return R.ok().put(Constant.DATA,${table.entityPath}Service.list(params));
        }

        /**
         * 新增${table.comment}
         */
        @PostMapping(value = "/add")
        @ApiOperation("新增${table.comment}")
        @Login
        public R add(@RequestBody ${entity} ${table.entityPath}) {
            ${table.entityPath}Service.save(${table.entityPath});
            return R.ok();
        }

        /**
         * 删除${table.comment}
         */
        @PostMapping(value = "/delete/{id}")
        @ApiOperation("删除${table.comment}")
        @Login
        public R delete(@PathVariable("id") Long id) {
    ${table.entityPath}Service.removeById(id);
            return R.ok();
        }

        /**
         * 修改${table.comment}
         */
        @PostMapping(value = "/update")
        @ApiOperation("修改${table.comment}")
        @Login
        public R update(@RequestBody ${entity} ${table.entityPath}) {
    ${table.entityPath}Service.updateById(${table.entityPath});
            return R.ok();
        }

        /**
         * 获取${table.comment}
         */
        @GetMapping(value = "/get/{id}")
        @ApiOperation("获取${table.comment}")
        @Login
        public R get(@PathVariable("id") Long id) {
            return R.ok().put("",${table.entityPath}Service.getById(id));
        }

}
</#if>

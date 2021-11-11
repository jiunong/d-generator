package ${test1};
package ${test1!};


import org.springframework.web.bind.annotation.RequestMapping;

<#if test>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

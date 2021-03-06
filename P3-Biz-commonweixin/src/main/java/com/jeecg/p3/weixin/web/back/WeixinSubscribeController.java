package com.jeecg.p3.weixin.web.back;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.jeecgframework.p3.core.common.utils.AjaxJson;
import org.jeecgframework.p3.core.logger.Logger;
import org.jeecgframework.p3.core.logger.LoggerFactory;
import org.jeecgframework.p3.core.util.SystemTools;
import org.jeecgframework.p3.core.util.plugin.ViewVelocity;
import org.jeecgframework.p3.core.utils.common.PageQuery;
import org.jeecgframework.p3.core.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeecg.p3.commonweixin.util.Constants;
import com.jeecg.p3.weixin.entity.WeixinSubscribe;
import com.jeecg.p3.weixin.service.WeixinSubscribeService;

/**
 * 描述：</b>关注欢迎语<br>
 * 
 * @author LeeShaoQing
 * @since：2018年07月20日 10时12分09秒 星期五
 * @version:1.0
 */
@Controller
@RequestMapping("/weixin/back/weixinSubscribe")
public class WeixinSubscribeController extends BaseController {

	public final static Logger log = LoggerFactory.getLogger(WeixinSubscribeController.class);
	@Autowired
	private WeixinSubscribeService weixinSubscribeService;

	/**
	 * 列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "list", method = { RequestMethod.GET,RequestMethod.POST })
	public void list(@ModelAttribute WeixinSubscribe query,HttpServletResponse response,HttpServletRequest request,
			@RequestParam(required = false, value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(required = false, value = "pageSize", defaultValue = "10") int pageSize)	throws Exception {
		PageQuery<WeixinSubscribe> pageQuery = new PageQuery<WeixinSubscribe>();
		String jwid =  request.getSession().getAttribute("jwid").toString();
	 	query.setJwid(jwid);
		pageQuery.setPageNo(pageNo);
		pageQuery.setPageSize(pageSize);
		VelocityContext velocityContext = new VelocityContext();
		pageQuery.setQuery(query);
		velocityContext.put("weixinSubscribe", query);
		velocityContext.put("pageInfos", SystemTools.convertPaginatedList(weixinSubscribeService.queryPageList(pageQuery)));
		String viewName = "weixin/back/weixinSubscribe-list.vm";
		ViewVelocity.view(request, response, viewName, velocityContext);
	}

	/**
	 * 详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "toDetail", method = RequestMethod.GET)
	public void weixinSubscribeDetail(@RequestParam(required = true, value = "id") String id, HttpServletResponse response, HttpServletRequest request)	throws Exception {
		VelocityContext velocityContext = new VelocityContext();
		String viewName = "weixin/back/weixinSubscribe-detail.vm";
		WeixinSubscribe weixinSubscribe = weixinSubscribeService.queryById(id);
		velocityContext.put("weixinSubscribe", weixinSubscribe);
		ViewVelocity.view(request, response, viewName, velocityContext);
	}

	/**
	 * 跳转到添加页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toAdd", method = { RequestMethod.GET,	RequestMethod.POST })
	public void toAddDialog(HttpServletRequest request,	HttpServletResponse response, ModelMap model) throws Exception {
		VelocityContext velocityContext = new VelocityContext();
		String viewName = "weixin/back/weixinSubscribe-add.vm";
		ViewVelocity.view(request, response, viewName, velocityContext);
	}

	/**
	 * 保存信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/doAdd", method = { RequestMethod.GET,	RequestMethod.POST })
	@ResponseBody
	public AjaxJson doAdd(@ModelAttribute WeixinSubscribe weixinSubscribe) {
		AjaxJson j = new AjaxJson();
		try {
			weixinSubscribe.setCreateTime(new Date());
			weixinSubscribeService.doAdd(weixinSubscribe);
			j.setMsg("保存成功");
		} catch (Exception e) {
			log.error(e.getMessage());
			j.setSuccess(false);
			j.setMsg("保存失败");
		}
		return j;
	}

	/**
	 * 跳转到编辑页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "toEdit", method = RequestMethod.GET)
	public void toEdit(@RequestParam(required = true, value = "id") String id, HttpServletResponse response, HttpServletRequest request) throws Exception {
		VelocityContext velocityContext = new VelocityContext();
		WeixinSubscribe weixinSubscribe = weixinSubscribeService.queryById(id);
		velocityContext.put("weixinSubscribe", weixinSubscribe);
		String viewName = "weixin/back/weixinSubscribe-edit.vm";
		ViewVelocity.view(request, response, viewName, velocityContext);
	}

	/**
	 * 编辑
	 * 
	 * @return
	 */
	@RequestMapping(value = "/doEdit", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public AjaxJson doEdit(@ModelAttribute WeixinSubscribe weixinSubscribe, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			weixinSubscribe.setUpdateTime(new Date());
			String updateBy = (String)request.getSession().getAttribute(Constants.SYSTEM_USERID);
			weixinSubscribe.setUpdateBy(updateBy);
			weixinSubscribeService.doEdit(weixinSubscribe);
			j.setMsg("编辑成功");
		} catch (Exception e) {
			log.error(e.getMessage());
			j.setSuccess(false);
			j.setMsg("编辑失败");
		}
		return j;
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "doDelete", method = RequestMethod.GET)
	@ResponseBody
	public AjaxJson doDelete(@RequestParam(required = true, value = "id") String id) {
		AjaxJson j = new AjaxJson();
		try {
			weixinSubscribeService.doDelete(id);
			j.setMsg("删除成功");
		} catch (Exception e) {
			log.error(e.getMessage());
			j.setSuccess(false);
			j.setMsg("删除失败");
		}
		return j;
	}
	
}

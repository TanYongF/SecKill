package fun.tans.seckill.config;

import com.alibaba.druid.util.StringUtils;
import fun.tans.seckill.domain.MiaoshaUser;
import fun.tans.seckill.service.MiaoshaUserService;
import fun.tans.seckill.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 具体作用是将每次请求传入的user信息（auth）,进行判定是否已登录，
 * 如果已经登陆，获取用户token并将其token过期时间更新
 * 如果未登录，返回null
 *
 * @Describe: 用户参数解析器
 * @Author: tyf
 * @CreateTime: 2022/4/17
 **/
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private MiaoshaUserService userService;

    /**
     * 指定解析MiaoshaUser类参数
     *
     * @param parameter the method parameter to check
     * @return 是否满足
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == MiaoshaUser.class;
    }

    //具体处理逻辑
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //前置处理
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        String paramToken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = RequestUtil.getCookieValue(request, MiaoshaUserService.COOKIE_NAME_TOKEN);

        //token为空，那么该用户未登录，返回null
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
    }

}

package com.ibaiq.security.config;

import com.ibaiq.security.auth.*;
import com.ibaiq.security.filter.TokenAuthenticationFilter;
import com.ibaiq.security.manager.UrlAccessDecisionManager;
import com.ibaiq.utils.encryption.MD5PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * SpringSecurity配置类
 *
 * @author 十三
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 认证失败处理器
     */
    @Autowired
    private WebAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 登录成功处理器
     */
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    /**
     * 登录失败处理器
     */
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    /**
     * 权限不足处理器
     */
    @Autowired
    private WebAccessDeniedHandler webAccessDeniedHandler;

    /**
     * 注销成功处理器
     */
    @Autowired
    private WebLogoutSuccessHandler webLogoutSuccessHandler;

    /**
     * url权限管理器
     */
    @Autowired
    private UrlAccessDecisionManager urlAccessDecisionManager;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭防火墙 允许跨域
        http.csrf().disable().cors();

        // 设置无session状态不需要session
        http.sessionManagement()
                          .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 配置权限
        http.authorizeRequests()
                          .anyRequest()
                          .authenticated()
                          .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                              @Override
                              public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                                  o.setAccessDecisionManager(urlAccessDecisionManager);
                                  return o;
                              }
                          });

        // 配置登录
        http.formLogin()
                          .successHandler(loginSuccessHandler)
                          .failureHandler(loginFailureHandler)
                          .permitAll()

                          .and()
                          .logout()
                          .logoutSuccessHandler(webLogoutSuccessHandler);

        // 配置权限不足处理和未认证
        http.exceptionHandling()
                          .accessDeniedHandler(webAccessDeniedHandler)
                          .authenticationEntryPoint(authenticationEntryPoint)

                          // 添加Token过滤器
                          .and()
                          .addFilter(tokenAuthenticationFilter());

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(authenticationProvider());
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        return new TokenAuthenticationFilter(authenticationManager());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);

        return provider;
    }

}

package com.example.springboothw.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class SoapConfig extends WsConfigurerAdapter {
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "catalog")
    public DefaultWsdl11Definition catalogWsdl11Definition(XsdSchema catalogXsdSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CatalogPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.springboothw.example.com/soap/catalog");
        wsdl11Definition.setSchema(catalogXsdSchema);
        return wsdl11Definition;
    }

    @Bean(name = "catalogXsdSchema")
    public XsdSchema catalogXsdSchema() {
        return new SimpleXsdSchema(new ClassPathResource("soap/catalog.xsd"));
    }

    @Bean(name = "order")
    public DefaultWsdl11Definition orderWsdl11Definition(XsdSchema orderXsdSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("OrderPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://www.springboothw.example.com/soap/order");
        wsdl11Definition.setSchema(orderXsdSchema);
        return wsdl11Definition;
    }

    @Bean(name = "orderXsdSchema")
    public XsdSchema orderXsdSchema() {
        return new SimpleXsdSchema(new ClassPathResource("soap/order.xsd"));
    }
}

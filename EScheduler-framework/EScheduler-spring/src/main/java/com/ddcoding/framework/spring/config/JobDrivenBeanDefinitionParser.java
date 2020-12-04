package com.ddcoding.framework.spring.config;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * 解析job-driven标签
 *
 */
public class JobDrivenBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        AbstractBeanDefinition beanDefinition = new GenericBeanDefinition();
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        beanDefinition.setBeanClass(SpringContextJobDriver.class);
        propertyValues.addPropertyValue("packagesToScan", element.getAttribute("packagesToScan"));
        beanDefinition.setPropertyValues(propertyValues);
        beanDefinition.setInitMethodName("init");
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, parserContext.getRegistry());
        return beanDefinition;
    }

}

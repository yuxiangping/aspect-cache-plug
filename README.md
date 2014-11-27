aspect-cache-plug
=================

Method aspect cache plug

可用于以下场景

查询接口：根据请求条件对返回结果进行缓存<br/>
更新接口：根据条件对指定缓存进行清除<br/>

```java
@Aspect(key = "query")
public Object query(String q) {
  // do something
  return null;
}
```

```java
@Aspect(key = "update")
public Object update(String q) {
  // do something  
  return null;
}
```


缓存query方法返回值
```xml
<bean class="com.aspect.config.CacheConfig">
	<property name="key" value="query"/>
	<property name="space" value="space2"/>
	<property name="keyExpression">
		<value>this[0]+new java.util.Date()</value>
	</property>					
	<property name="timeout" value="60"/>
</bean>
```

执行update时清除query的返回结果
```xml
<bean class="com.aspect.config.ClearConfig">
    <property name="key" value="update"/>    
    <property name="cachedConfigs">
	    <list>
	        <value>query</value>
	    </list>
	</property>
</bean>
```

加入spring支持
```xml
<aop:config> 
	    <aop:pointcut id="adviceAspectPoint" expression="execution(* com.sample..*.*(..)) and @annotation(Aspect)" /> 
		<aop:aspect ref="adviceAspect"> 
			<aop:around method="execute" pointcut-ref="adviceAspectPoint"  arg-names="Aspect" />
		</aop:aspect> 
	</aop:config>
    
    <bean id="adviceAspect" class="com.aspect.AdviceAspect">
		<property name="rootKey" value="aspect.cache."/>
		<property name="cache" ref="cache"/>
		<property name="caches">
		    <list>
		        <bean class="com.aspect.config.CacheConfig">
		            <property name="key" value="queryMethod1"/>
		            <property name="space" value="space1"/>
					<property name="keyExpression">
						<value>this[1]+"."+this[2]</value>
					</property>
					<property name="domainExpression">
						<value>this[0]</value>
					</property>
					<property name="timeout" value="60"/>
		        </bean>
		        <bean class="com.aspect.config.CacheConfig">
		            <property name="key" value="queryMethod2"/>
		            <property name="space" value="space1"/>
					<property name="keyExpression">
						<value>this[0]</value>
					</property>					
					<property name="timeout" value="60"/>
		        </bean>
		        <bean class="com.aspect.config.CacheConfig">
		            <property name="key" value="space2QueryMethod1"/>
		            <property name="space" value="space2"/>
					<property name="keyExpression">
						<value>this[0]+new java.util.Date()</value>
					</property>					
					<property name="timeout" value="60"/>
		        </bean>
		    </list>
		</property>
		<property name="clears">
		    <list>
		        <bean class="com.aspect.config.ClearConfig">
		            <property name="key" value="updateMethod1"/>
		            <property name="domainExpression">
						<value>this[0]</value>
					</property>
					<property name="cachedConfigs">
					    <list>
					        <value>queryMethod1</value>
					        <value>space2QueryMethod1</value>
					    </list>
					</property>
		        </bean>
		    </list>
		</property>
    </bean>
```

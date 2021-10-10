<#assign D = "$" />
<#assign J = "#" />
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.3.3.RELEASE</version>
		<relativePath />
	</parent>

	<groupId>${gp.groupId}</groupId>
	<artifactId>${gp.name}</artifactId>
	<version>${gp.version}</version>
	<packaging>jar</packaging>
	<name>${gp.name}</name>
	<description>${gp.note!}</description>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
		<maven.build.timestamp.format>yyyy-MM-dd HH:mm:ss</maven.build.timestamp.format>
	</properties>
	<#if gp.mavenModule && gp.mavenServer??>
	<distributionManagement>
		<repository>
			<id>releases</id>
			<url>${gp.mavenServer}/repository/maven-releases/</url>
		</repository>
		<snapshotRepository>
			<id>snapshots</id>
			<url>${gp.mavenServer}/repository/maven-snapshots/</url>
		</snapshotRepository>
	</distributionManagement>
	</#if>

	<dependencies>
		<#if gp.springModule || gp.name=='framework'>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>2.1.3</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<optional>true</optional>
			<scope>runtime</scope>
		</dependency>
		</#if>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<#if gp.addtionalDependencies ??>
		${gp.addtionalDependencies}
		</#if>
		<#if gp.test?? && gp.test>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
			<exclusions>
				<exclusion>
					<groupId>org.junit.vintage</groupId>
					<artifactId>junit-vintage-engine</artifactId>
				</exclusion>
			</exclusions>
		</dependency>
		<#if gp.name=='framework'>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>
		</#if>
        </#if>
		<#if gp.modules??>
		<#list gp.modules?split(',') as m>
		<dependency>
			<groupId>top.iotequ</groupId>
			<artifactId>${m}</artifactId>
			<version>${mdVersion}</version>
		</dependency>
		</#list>
		</#if>
	</dependencies>
	<#if gp.mavenModule && gp.springModule>
	<profiles>
		<profile>
			<id>spring</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<properties>
				<build.groupId>org.springframework.boot</build.groupId>
				<build.artifactId>spring-boot-maven-plugin</build.artifactId>
				<build.excludes></build.excludes>
				<resource.excludes></resource.excludes>
			</properties>
		</profile>
		<profile>
			<id>maven</id>
			<activation>
				<activeByDefault>false</activeByDefault>
			</activation>
			<properties>
				<build.groupId>org.apache.maven.plugins</build.groupId>
				<build.artifactId>maven-compiler-plugin</build.artifactId>
				<build.excludes>${gp.groupId?replace('.','/')}/${camelName?cap_first}Application.java</build.excludes>
				<resource.excludes>application.yml</resource.excludes>
			</properties>
		</profile>
	</profiles>
	</#if>
	<build>
		<resources>
			<resource>
				<directory>src/main/resources</directory>
				<#if gp.mavenModule && gp.springModule>
				<excludes>
					<exclude>${D}{resource.excludes}</exclude>
				</excludes>
				</#if>
			</resource>
		</resources>
		<finalName>${D}{project.artifactId}-${D}{project.version}</finalName>
		<plugins>
			<plugin>
			<#if gp.mavenModule && gp.springModule>
				<groupId>${D}{build.groupId}</groupId>
				<artifactId>${D}{build.artifactId}</artifactId>
				<configuration>
					<excludes>${D}{build.excludes}</excludes>
				</configuration>
			<#elseif gp.mavenModule>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
			<#else>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</#if>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-javadoc-plugin</artifactId>
				<configuration>
					<quiet>true</quiet>
					<encoding>UTF-8</encoding>
					<charset>UTF-8</charset>
					<docencoding>UTF-8</docencoding>
				</configuration>
				<executions>
					<execution>
						<id>attach-javadocs</id>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-source-plugin</artifactId>
				<executions>
					<execution>
						<id>attach-sources</id>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<configuration>
					<archive>
						<manifestEntries>
							<Iotequ-Module>true</Iotequ-Module>
							<Group-Id>${D}{project.groupId}</Group-Id>
							<Artifact-Id>${D}{project.artifactId}</Artifact-Id>
							<Version>${D}{project.version}</Version>
							<Build-Time>${D}{maven.build.timestamp}</Build-Time>
						</manifestEntries>
					</archive>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>


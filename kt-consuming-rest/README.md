# Spring・SpringBootをkotlinでやってみる 第１回 ~ 外部のAPI呼び出し ~

## はじめに

このシリーズではSpring・SpringBootの[公式ガイド](https://spring.io/guides)を参考にKotlinでチュートリアルを行なっていきます。  

今回は[外部のAPI呼び出し](https://spring.io/guides/gs/consuming-rest/)をベースにコマンドラインから叩くAPIクライアントを作成しましょう。

## シリーズ記事一覧

## 必要環境

* JDK1.8以上
* Gradle4以上
* Sprint Tool Suite もしくは InteliJ IDEA
* Kotlin

## Spring Initializr

[Spring Initializr](https://start.spring.io/)で初期モジュールのダウンロードを行います。

以下のような感じで。必要な依存はSpring Webのみです。

<img width="1239" alt="スクリーンショット 2019-11-10 9.25.35.png" src="https://qiita-image-store.s3.ap-northeast-1.amazonaws.com/0/187619/17999d6c-865f-ad42-8d65-1074f5ae0724.png">

ダウンロードが完了したら、ご使用のIDEにインポートしておきましょう。

## データを格納するドメインクラスの作成

今回はこちらのAPIを使用します。

https://gturnquist-quoters.cfapps.io/api/random

```
{"type":"success","value":{"id":5,"quote":"Spring Boot solves this problem. It gets rid of XML and wires up common components for me, so I don't have to spend hours scratching my head just to figure out how it's all pieced together."}}
```
いい感じにランダムな値が帰ってきます。

ではまず、上記の形式のJosnデータを格納するドメインクラスを作成しましょう。

```src/main/kotlin/com/example/ktconsumingrest/Quote.kt
package com.example.ktconsumingrest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties
class Quote(
    var type: String,
    var value: Value
) {
    override fun toString(): String {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
```

```src/main/kotlin/com/example/ktconsumingrest/Value.kt
package com.example.ktconsumingrest

class Value(
    var id: Long,
    var quote: String
) {
    override fun toString(): String {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}
```

## API呼び出し部分の作成

spring bootのメインクラス、、@SpringBootApplicationのアノテーションがあるクラスを以下のように編集しましょう(この例ではKtConsumingRestApplication.kt)

```src/main/kotlin/com/example/ktconsumingrest/KtConsumingRestApplication.kt
package com.example.ktconsumingrest

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.client.RestTemplateBuilder



@SpringBootApplication
class KtConsumingRestApplication {

	private val logger = LoggerFactory.getLogger(KtConsumingRestApplication::class.java)

	@Bean
	fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
		return builder.build()
	}

	@Bean
	@Throws(Exception::class)
	fun run(restTemplate: RestTemplate) = CommandLineRunner {
		val quote = restTemplate.getForObject(
				"https://gturnquist-quoters.cfapps.io/api/random",
					Quote::class.java)
		logger.info(quote!!.toString())
	}
}

fun main(args: Array<String>) {
	runApplication<KtConsumingRestApplication>(*args)
}
```

## 実行する

ビルドします。今回は実行可能なjarを準備し、こちらを叩くことによってアプリケーションを動作させます。  

アプリケーションのルートディレクトリで以下のコマンドを実行します。

```
$ ./gradlew build
```

jarを実行します。

```
$ java -jar build/libs/kt-consuming-rest-0.0.1-SNAPSHOT.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.1.RELEASE)

2019-11-10 11:23:35.318  INFO 49218 --- [           main] c.e.k.KtConsumingRestApplicationKt       : Starting KtConsumingRestApplicationKt on LAB-N1253.local with PID 49218 (/develop/learn/spring/kotlin-spring-guides/kt-consuming-rest/build/libs/kt-consuming-rest-0.0.1-SNAPSHOT.jar started by ishizukayusuke in /develop/learn/spring/kotlin-spring-guides/kt-consuming-rest)
2019-11-10 11:23:35.322  INFO 49218 --- [           main] c.e.k.KtConsumingRestApplicationKt       : No active profile set, falling back to default profiles: default
2019-11-10 11:23:36.772  INFO 49218 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2019-11-10 11:23:36.788  INFO 49218 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2019-11-10 11:23:36.788  INFO 49218 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.27]
2019-11-10 11:23:36.872  INFO 49218 --- [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2019-11-10 11:23:36.872  INFO 49218 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1441 ms
2019-11-10 11:23:37.126  INFO 49218 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2019-11-10 11:23:37.324  INFO 49218 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2019-11-10 11:23:37.332  INFO 49218 --- [           main] c.e.k.KtConsumingRestApplicationKt       : Started KtConsumingRestApplicationKt in 2.656 seconds (JVM running for 3.223)
2019-11-10 11:23:38.796  INFO 49218 --- [           main] c.e.k.KtConsumingRestApplication         : Quote{type='success', value=Value{id=3, quote='Spring has come quite a ways in addressing developer enjoyment and ease of use since the last time I built an application using it.'}}
```

```
Quote{type='success', value=Value{id=3, quote='Spring has come quite a ways in addressing developer enjoyment and ease of use since the last time I built an application using it.'}}
```

↑の部分がAPI呼び出し結果になります。


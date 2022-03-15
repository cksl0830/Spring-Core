## < 좋은 객체지향 설계 >

* **다형성**의 본질 
```
- 인터페이스를 구현한 객체 인스턴스를 실행 시점에 유연하게 변경할 수 있다.
- 클라이언트를 변경하지 않고, 서버의 구현 기능을 유연하게 변경할 수 있다.
```
> 인터페이스를 안정적으로 설계하는 것과 다형성이 중요하다.


- **SOLID** 원칙
```
1. SRP: 단일 책임 원칙 - 하나의 클래스는 하나의 책임만 가진다. ( 기준은 **변경** )
   ex) 무언가 변경이 있을 때, 파급 효과가 적으면 원칙을 잘 지킨것 ! 
2. ⭐️OCP: 개방-폐쇄 원칙 - 확장에는 열려 있으나 변경에는 닫혀 있어야 한다.
   +) 객체를 생성하고, 연관관계를 맺어주는 별도의 조립, 설정자가 필요하다. 
3. LSP: 리스코프 치환 원칙 - 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타입의 인스턴스로 바꿀 수 있어야 한다.
4. ISP: 인터페이스 분리 원칙 - 특정 클라이언트를 위해 하나의 인터페이스를 여러 개의 인터페이스로 분리
5. ⭐️DIP: 의존관계 역전 원칙 - 구현 클래스에 의존하지 말고, 인터페이스에 의존하라는 뜻이다.
   +) OCP를 사용하면 DIP원칙을 위반하게 된다.
```
> 다형성만으로 OCP, DIP를 지킬 수 없기에 **_스프링의 DI_** 를 사용하여 가능하게 함



## < 순수 자바 코드로만 DI 적용 >

- #### 정적 클래스 의존 관계
![image](https://user-images.githubusercontent.com/60590737/157240208-7007b1f6-b15a-4331-b8cc-60b3655d881a.png)

- #### 동적 객체 인스턴스 의존 관계 (**의존관계주입**)
![image](https://user-images.githubusercontent.com/60590737/157240414-42f25784-df80-40b9-9f71-c11d825d8a6a.png)

- 문제점   
1. 새로 개발한 정률 할인 정책을 적용하려고 하니 클라이언트 코드인 주문 서비스 구현체도 함께 변경해야했다 :: OCP 위반    
2. 주문 서비스 클라이언트가 인터페이스인 DiscountPolicy 뿐만 아니라, 구체 클래스인 FixDiscountPolicy도 함께 의존한다 :: DIP 위반    

- **AppConfig** 로 해결       
사용 영역과, 객체를 생성하고 구성하는 영역으로 분리 -> 할인 정책을 변경해도 AppConfig가 있는 구성 영역만 변경, 사용 영역 변경 X           
-> **_이를 DI 컨테이너라고 한다._**     

- 스프링으로 전환    
```
@Configuration : AppConfig에 설정을 구성한다는 뜻의 @Configuration 을 붙여준다.    
@Bean : 각 메서드에 @Bean 을 붙여준다. 이렇게 하면 스프링 컨테이너에 스프링 빈으로 등록한다.    
ApplicationContext : 스프링 컨테이너 - >  AppConfig 를 사용해서 직접 객체를 생성하고 DI 했었지만, 이제 스프링 컨테이너를 통해서 사용한다.    
applicationContext.getBean() : 스프링 빈은 이 메서드를 사용해서 찾을 수 있다.
* 참고: ApplicationContext 의 인터페이스 = BeanFactory 
```

## < 스프링 컨테이너와 스프링 빈 > 

- 빈 출력하기 
ac.getBeanDefinitionNames() : 스프링에 등록된 모든 빈 이름을 조회       
ac.getBean(빈이름, 타입) : 빈 이름으로 빈 객체(인스턴스)를 조회      
ac.getBean(타입) : 타입으로 조회시 같은 타입의 스프링 빈이 둘 이상이면 오류가 발생    
ac.getBeansOfType(클래스) : 해당 타입의 모든 빈을 조회    
```
부모 타입으로 조회하면, 자식 타입도 함께 조회
그래서 모든 자바 객체의 최고 부모인 Object 타입으로 조회하면, 모든 스프링 빈을 조회
```

- 애플리케이션 빈 출력하기 (스프링이 내부에서 사용하는 빈은 제외하고, 내가 등록한 빈만 출력)    
```
ROLE_APPLICATION : 일반적으로 사용자가 정의한 빈 
ROLE_INFRASTRUCTURE : 스프링이 내부에서 사용하는 빈
```

#### 전체흐름     
> AnnotationConfigApplicationContext 는 AnnotatedBeanDefinitionReader 를 사용해서 AppConfig.class 를 읽고 BeanDefinition 을 생성한다.     



## < 싱글톤 컨테이너 > 

- **문제점** : 스프링 없는 순수한 DI 컨테이너인 AppConfig는 요청을 할 때 마다 객체를 새로 생성 -> 메모리낭비 

- **싱글톤 패턴이란** : 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴!    
```
- 스프링 컨테이너
1. 스프링 컨테이너는 싱글턴 패턴을 적용하지 않아도, 객체 인스턴스를 싱글톤으로 관리한다.
2. 스프링 컨테이너는 싱글톤 컨테이너 역할을 한다. 이렇게 싱글톤 객체를 생성하고 관리하는 기능을 싱글톤 레지스트리라 한다.
3. DIP, OCP, 테스트, private 생성자로 부터 자유롭게 싱글톤을 사용할 수 있다.
```

- **주의점** : 싱글톤 패턴이든, 스프링 같은 싱글톤 컨테이너를 사용하든, 객체 인스턴스를 하나만 생성해서 공유하는 싱글톤 방식은 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 싱글톤 객체는 상태를 유지(stateful)하게 설계하면 안된다! **_무상태(stateless)_** 로 설계해야 한다!    
```
1. 특정 클라이언트에 의존적인 필드가 있으면 안된다.
2. 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
3. 가급적 읽기만 가능해야 한다.
4. 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다.
```

- **중요**      
1. @Bean 이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.
2. @Bean 만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.    

   따라서, **_@Configuration_** 을 사용하여 싱글톤 보장! 


## < 컴포넌트 스캔 > 

- **@ComponentScan 은 @Component 가 붙은 모든 클래스를 스프링 빈으로 등록한다.**  
1. AppConfig 같은 설정 정보에 @ComponentScan 을 붙여준다
2. 각 클래스 구현체에 @Component 애노테이션을 붙여준다
3. 생성자에 @Autowired 를 붙여서 의존관계를 자동으로 주입해준다  //getBean(MemberRepository.class) 와 동일
4. @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다 (프로젝트 최상단 위치에 두는 것이 좋음)

- **충돌**  
1. 자동 vs 자동 : ConflictingBeanDefinitionException 예외 발생
2. 수동 vs 자동 : 수동 빈이 자동 빈을 오버라이딩 해버린다 (수동 우선권)   
:: 스프링부트에서 오류를 발생한다.

## < 의존관계 주입 > 

- 생성자 주입 (권장)  
```
1. 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다
2. 불변, 필수 의존관계에 사용
3. 생성자가 딱 1개만 있으면 @Autowired를 생략해도 자동 주입
⭐️ 4. 생성자 주입을 사용하면 필드에 final 키워드를 사용할 수 있다
```

- 수정자 주입(setter 주입)  
```
1. 선택, 변경 가능성이 있는 의존관계에 사용
2. 주입할 대상이 없어도 동작하게 하려면 @Autowired(required = false) 로 지정
3. 생성자가 딱 1개만 있으면 @Autowired를 생략해도 자동 주입
```

- 옵션처리   
```
1. @Autowired(required=false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨 
2. org.springframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력된다. 
3. Optional<> : 자동 주입할 대상이 없으면 Optional.empty 가 입력된다.
```

- lombok 라이브러리    
**@RequiredArgsConstructor 기능을 사용하면 final이 붙은 필드를 모아서 생성자를 자동으로 만들어준다**

- 조회 빈이 2개 이상일 때 - 문제  
1. @Autowired 는 타입(Type)으로 조회 (ac.getBean(DiscountPolicy.class 와 유사함)  
2. 예를 들어, DiscountPolicy 의 하위 타입인 FixDiscountPolicy , RateDiscountPolicy 둘다 스프링 빈으로 선언하면  
  --> 누구를 주입할지 알 수 없음  

- 조회 빈이 2개 이상일 때 - 해결  
1. @Qualifier : 빈 등록시 @Qualifier를 붙여 주고, 주입시에 @Qualifier를 붙여주고 등록한 이름을 적어준다. **(@Qualifier끼리 매칭)**   
2. @Primary : @Primary 는 우선순위를 정하는 방법이다. @Autowired 시에 여러 빈이 매칭되면 @Primary 가 우선권을 가진다.  

- #### 정리  
```
1. 조회 빈이 모두 필요할 때 **List, Map**  을 사용하자! 
2. 편리한 자동 기능을 기본으로 사용하자! (업무로직 ex. 서비스, 컨트롤러, 비즈니스 요구사항 등..)
3. 직접 등록하는 기술 지원 객체는 수동 등록하자! (기술지원로직 ex. AOP, DB연결, 공통 로그 처리 등..)
4. 다형성을 적극 활용하는 비즈니스 로직은 수동 등록을 고민해보자!
```


## < 빈 생명주기 콜백 >

#### 데이터베이스 커넥션 풀이나, 네트워크 소켓처럼 애플리케이션 시작 시점에 필요한 연결을 미리 해두고, 애플리케이션 종료 시점에 연결을 모두 종료하는 작업을 진행하려면, 객체의 초기화와 종료 작업이 필요하다.   

- 스프링 빈의 이벤트 라이프사이클  
**스프링컨테이너 생성 - 스프링빈 생성 - 의존관계 주입 - 초기화콜백 - 사용 - 소멸전콜백 - 스프링 종료**
> 초기화 콜백: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출   
> 소멸전 콜백: 빈이 소멸되기 직전에 호출

- 방법   
```
1. @PostConstruct(초기화 콜백), @PreDestroy(소멸전 콜백) 애노테이션을 사용하자
2. 코드를 고칠 수 없는 외부 라이브러리를 초기화, 종료해야 하면 @Bean 의 initMethod , destroyMethod 를 사용하자.
```

## < 빈 스코프 > 

- 싱글톤타입 스코프: 스프링 컨테이너에 계속 요청이 와도 같은 객체 인스턴스의 스프링 빈을 반환  
- 프로토타입 스코프: 스프링 컨테이너는 프로토타입 빈을 생성하고, 의존관계 주입, 초기화까지만 처리 즉, **요청할 때 마다 새로 생성**      
  --> 클라이언트가 관리해야하고 @PreDestroy 같은 종료 메서드가 호출되지 않는다.  
- 함께 사용시 : 싱글톤 빈은 생성 시점에만 의존관계 주입을 받기 때문에, 프로토타입 빈이 새로 생성되기는 하지만, 싱글톤 빈과 함께 계속 유지되어서 같은 프로토타입 빈 사용   

- 해결 방법   
1. ObjectFactory, ObjectProvider (스프링에 의존)  
```
private ObjectProvider<PrototypeBean> prototypeBeanProvider; 
PrototypeBean prototypeBean = prototypeBeanProvider.getObject(); // 항상 새로운 빈 생성 
```

2. javax.inject.Provider (라이브러리 필요 / 자바 표준: 스프링이 아닌 다른 컨테이너에서도 사용 가능)     
```
private Provider<PrototypeBean> provider;
PrototypeBean prototypeBean = provider.get(); // 항상 새로운 빈 생성 
```

## < 웹 스코프 > 

- Request scope    
**HTTP 요청 하나가 들어오고 나갈 때 까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고, 관리된다.**   
```
@Scope(value = "request") :: HTTP 요청 당 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸
ObjectProvider를 사용해야 빈 생성 정상처리 가능
```
> ![image](https://user-images.githubusercontent.com/60590737/158412053-5b0919bc-cec6-402e-8fc5-032413cad3d5.png)

- 스코프와 프록시 (싱글톤 빈처럼 사용 가능하나 다름! 주의!)
```
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)  //인터페이스면 INTERFACES
ObjectProvider 해주지 않아도 가짜 프록시 개체를 생성하여 주입
```


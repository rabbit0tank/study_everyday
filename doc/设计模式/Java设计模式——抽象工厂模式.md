# Java设计模式——抽象工厂模式

## 1.抽象工厂

​		提供一个创建一系列相关或相互依赖对象的接口，而无需指定他们具体的类

​		不同于工厂方法，抽象工厂他针对的是一个2级产品工厂，首先我们的类会被分成多个抽象产品，每个抽象产品又有很多型号的产品。同样是水果工厂，如果你想要的只是苹果这个水果，工厂模式已经足够用了，但是要是你的要求高了，只想要红富士的苹果，就需要利用 抽象工厂模式。来找到对应生产苹果的公司，想找到红富士苹果所在的产品族，之前提到的一个水果工厂只能生产一个水果，而抽象水果工厂能生产所有水果，但是由于地域特点（实际实现具体工厂），他们产出的水果各不相同，有的工厂的苹果是红富士的，有的就可能是其他品牌的。

​		当你需要红富士苹果。首先我们需要在全国水果工厂（实现 **生成所有种类水果的抽象工厂** 的所有具体工厂类）找到能产出红富士苹果这个品种的地方（**能产生红富士苹果类的具体工厂类**），在能产出红富士苹果的地方，当然不止生产红富士苹果，还有其他固定品种的橘子呀，梨之内的，然后在那个地方找到生成苹果的机器，启动它（**调用产生苹果类的方法**）就可以获得到红富士苹果了。

​		一个点菜员只负责一个套餐，每一个套餐就是一个产品族，里面的主食、饮料就是产品等级结构。

​		**抽象工厂**模式简化后，如果只有一个产品等级结构，那就是**工厂模式**。

抽象工厂的一般包含

**1.AbstractFactory(抽象工厂)：**它声明了一组用于创建一组产品的方法，每一个方法对应一种产品。

**2.ConcreteFactory(具体工厂)：**它实现了抽象给工厂的所有方法，每个方法对应的就是抽象工厂生成的具体对象。

**3.AbstractProduct(抽象产品)：**他为每种产品声明了接口，在抽象产品中声明了产品所具有的业务员方法。（和工厂方法一致，不同的是如果工厂之前只能生产一种水果，那么抽象工厂就能生产所有水果，因此当前的抽象产品就应该是苹果，而不是水果。之前抽象产品的是一个大类，现在是大类对应的小类，而我们提取的不再是小类的公共特点，而是小类的不同品种之间的公共特点。小类的公共特点不再是我们找到对应产品的依据，因为所有的工厂都能生产出我们想要的水果，我们不需要根据比较抽象的水果特点来获取我们想要的产品，而是直接告诉工厂我们想要苹果，再像之前那样提出苹果的红富士品种的特点，即小类品种的公共特点属性表）

**4.ConcreteProduct(具体产品)：**它定义具体共产生产出的具体产品对象。（抽象工厂模式下，对象更加明确，存在不同具体产品对应不同的抽象产品。在工厂模式下，所有的具体产品对应一个抽象产品。）



## 2.抽象工厂的特点

1)优点：隔离了具体类的生成；一个产品族种的多个对象被设计成一起工作时，能保证客户端始终只使用一个产品族的对象。（用水果举例来说，就是你要是只想吃新疆的水果，使用抽象工厂类就会避免你吃到其他地方（比如广东）的水果。）；增加新的产品族很方便，符合开闭原则。（你可以随心所欲在其他地方创建水果工厂，只要你愿意，就可以利用抽象工厂的模板在北京复刻一个和新疆水果工厂相似的水果工厂）

2)缺点：增加新的产品级会很麻烦，需要对抽象工厂做改动，还需要新增一个抽象类。（比如你突然想吃香蕉，但是最初设计工厂模板的时候就没涉及到生产香蕉这种水果，于是你就需要紧急告诉全国所有工厂都按照自己的地域特点生产出你想要的香蕉，你再根据你的需求在所有的工厂中选择你想要的香蕉品种。）

3）使用场景：一个系统不依赖于实例如何被创建、组合、表达的细节，将对象的创建和使用解耦；系统中有多余一个的产品族（A品种苹果、B品种香蕉、C品种梨就是一个产品族，假设水果工厂只生产苹果、香蕉、梨），而每次只是使用产品族中的一个产品；同一个产品族的产品要求需要一起使用，而且不允许其他产品族的介入，系统设计时对产品进行归组，但是每个组的都有和其他组对应的产品具有相同的特点；产品登记结构稳定，不会额外添加非产品族中的对象。
package dubbospi.main;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import dubbospi.interf.CarColor;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        //加载接口的实现类
        ExtensionLoader<CarColor> loader = ExtensionLoader.getExtensionLoader(CarColor.class);

        Map<String,String> map = new HashMap<>();
        map.put("car","red");
        URL url = new URL("","",1,map);
        //此时先执行了benz，然后才执行他依赖的red实现类
        CarColor red = loader.getExtension("benz");
        //url -- dubbo的总线
        red.printColor(url);
    }
}

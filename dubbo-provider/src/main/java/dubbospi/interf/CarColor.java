package dubbospi.interf;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
@SPI
public interface CarColor {
    @Adaptive("car")
    void printColor(URL url);
}

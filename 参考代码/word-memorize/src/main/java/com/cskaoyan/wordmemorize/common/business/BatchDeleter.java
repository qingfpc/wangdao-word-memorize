package com.cskaoyan.wordmemorize.common.business;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.tomcat.util.net.Acceptor;

import java.util.List;

public interface BatchDeleter<T> {

    <P> void delete(SFunction<T,P>field, List<P> conditions);
}

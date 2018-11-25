
package com.demo.common.util.batch;
import java.io.File;

//监控文件变化
public interface FileListener {
	// 当文件修改时间发生变化时，触发该函数
	public void fileChanged(File file);
}
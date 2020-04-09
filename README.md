# SimpleHttpFrame
简单的Http请求框架，支持请求json和图片数据

## 要点
* 构建者模式设置请求相关（待实现：由调用层设置）
* 线程池管理请求线程，单例模式实现
* 请求任务实现Delayed接口，来支持请求失败重试
* 使用HttpURLConnection来发起网络请求

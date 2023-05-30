# AscentSys
艾斯医药系统
2023-05-30 重新上传
此版本：
  1、有聊天窗口
  2、数据存在txt文件
  3、图片和文件路径是相对于当前根目录的路径（如有需要自行修改）
  4、虽然有config配置文件，那是使用mysql遗留的，不用管它
运行：
  1、启动服务端 src/com/ascent/utils/ProductDataServer.java 
  2、启动登录页面 src/com.ascent.ui/LoginFrame.java
修改权限：
  user.txt 每行表示一个用户，字段分别是用户名、密码、权限（0普通用户 1管理员）

import Vue from "vue";
import { Button, Form, FormItem, Input, Message } from "element-ui";


Vue.use(Button);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Input);
//Message挂载到Vue原型对象上, $message为自定义属性
Vue.prototype.$message = Message;

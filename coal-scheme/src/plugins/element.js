import Vue from "vue";
//1. 引入element ui 组件
import {
  Button,
  Form,
  FormItem,
  Input,
  Message,
  Container,
  Header,
  Aside,
  Main,
  Menu,
  Submenu,
  MenuItemGroup,
  MenuItem,
  Timeline,
  TimelineItem,
  Card,
  Table,
  TableColumn,
  MessageBox,
  Dialog,
  Slider,
  Progress,
  InputNumber,
  Tag,
  Loading,
  Notification,
  Avatar,
} from "element-ui";


//2.全局注册
Vue.use(Button);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Input);
Vue.use(Container);
Vue.use(Header);
Vue.use(Aside);
Vue.use(Main);
Vue.use(Menu);
Vue.use(Submenu);
Vue.use(MenuItemGroup);
Vue.use(MenuItem);
Vue.use(Card);
Vue.use(Timeline);
Vue.use(TimelineItem);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Dialog);
Vue.use(Slider);
Vue.use(Progress);
Vue.use(InputNumber);
Vue.use(Tag);
Vue.use(Avatar);


//Message挂载到Vue原型对象上, $message为自定义属性
//Vue.prototype.$button = Button;
Vue.prototype.$message = Message;
Vue.prototype.$messageBox = MessageBox;
//Vue.prototype.$msgbox = MessageBox;
Vue.prototype.$alert = MessageBox.alert;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$prompt = MessageBox.prompt;
Vue.prototype.$confirm = MessageBox.confirm;
Vue.prototype.$loading = Loading.service;

Vue.prototype.$notify = Notification;
Vue.prototype.$message = Message;
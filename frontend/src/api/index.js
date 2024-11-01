const api = {
  Login: '/auth/login',
  Logout: '/auth/logout',
  ForgePassword: '/auth/forge-password',
  Register: '/auth/register',
  twoStepCode: '/auth/2step-code',
  SendSms: '/account/sms',
  SendSmsErr: '/account/sms_err',
  // get my info
  UserInfo: '/user/info',

  // 下面是自己的用户认证的接口
  UserRegister: '/user/register',
  UserLogin: '/user/login',

  // 考试的接口
  ExamQuestionList: '/exam/question/list',
  ExamQuestionAll: '/exam/question/all',
  ExamQuestionUpdate: '/exam/question/update',
  ExamQuestionSelection: '/exam/question/selection',
  ExamQuestionCreate: '/exam/question/create',
  ExamList: '/exam/list',
  ExamAll: '/exam/all',
  // 获取问题列表，按照单选、多选和判断进行分类
  ExamQuestionTypeList: '/exam/question/type/list',
  ExamCreate: '/exam/create',
  ExamUpdate: '/exam/update',
  ExamCardList: '/exam/card/list',
  // 获取考试详情
  ExamDetail: '/exam/detail/',
  // 获取考试详情
  QuestionDetail: '/exam/question/detail/',
  // 交卷
  FinishExam: '/exam/finish/',
  ExamRecordList: '/exam/record/list',
  recordDetail: '/exam/record/detail/',
  Classify: '/model/classify',
  GiftList: '/gift/list',
  CreateGiftOrder: '/gift/order/new',
  UploadFile: '/v2/upload',
  GetGiftOrderList: '/gift/order/list',
  CreateGift: '/gift/new',
  UpdateGift: '/gift/update',
  DelGift: '/gift/del'
}
export default api

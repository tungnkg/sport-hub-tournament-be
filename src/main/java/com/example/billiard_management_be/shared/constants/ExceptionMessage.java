package com.example.billiard_management_be.shared.constants;

public final class ExceptionMessage {
  public static final String USER_IS_NOT_EXIST = "không tồn tại người dùng";
  public static final String PASSWORD_INCORRECT = "Mật khẩu không chính xác";
  public static final String LOGIN_FAIL = "Đăng nhập thất bại";
  public static final String INVALID_PASSWORD = "Mật khẩu không hợp lệ";
  public static final String REFRESH_TOKEN_NOT_VALID = "Refresh token không hợp lệ";
  public static final String NOT_VALID_USER_DETAILS = "Thông tin người dùng hiện tại không hợp lệ";
  public static final String OLD_PASSWORD_NOT_TRUE = "Mật khẩu cũ không đúng";
  public static final String USER_NOT_IS_TOURNAMENT = "User không phải là đơn vị tổ chức";
  public static final String COMMENT_NOT_EXIST = "Bạn không được phép chỉnh sửa comment này";
  public static final String CANNOT_DELETE_COMMENT = "Bạn không được xoá comment này";
  public static final String USER_NOT_FOUND = "Không tìm được thông tin cơ thủ";
  public static final String ROLE_NOT_FOUND = "Không tìm được thông tin role";
  public static final String TOURNAMENT_NOT_FOUND = " Không tìm được thông tin tournament";

  public static final String TOURNAMENT_DATE_INVALID = "Ngày bắt đầu không được lớn hơn ngày kết thúc";
  public static final String TOURNAMENT_REGISTERED = "Người chơi đã đăng ký giải đấu này";
  public static final String TOURNAMENT_FULL =  "Giải đấu đã đủ số lượng người chơi";
  public static final String TOURNAMENT_REGISTER_NOT_FOUND = "Không tìm thấy thông tin đăng ký giải đấu";
  public static final String ROUND_NOT_VALID = "Vòng thi đấu không hợp lệ";
  public static final String PLAYERS_NOT_EQUAL = "Cac cơ thủ không được trùng nhau";
  public static final String PAIR_NOT_FOUND = "Không tìm thấy thông tin cặp thi đấu";
  public static final String PLAYER_WINNER_NOT_IN_PAIR = "Người chơi không nằm trong cặp thi đấu";
  public static final String RESULT_NOT_VALID = "Kết quả không hợp lệ";

  public static final String PAYMENT_NOT_FOUND = " Không tìm thấy thông tin thanh toán";
  public static final String VNPAY_PAYMENT_SUCCESS = "Thanh toán thành công";
    public static final String VNPAY_PAYMENT_FAIL = "Thanh toán thất bại";

}

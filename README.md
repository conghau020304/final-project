*Chức năng cơ bản:
--------------------
- Quản lý danh sách hàng hóa
	+ Thêm/sửa/xóa/tìm kiếm hàng hóa (đã làm)
- Quản lý danh sách hóa đơn (đã làm)
	+ Thêm mới hóa đơn, thêm danh sách hàng và số lượng vào hóa đơn, tự cộng tổng giá trị đơn hàng, (đã làm)
  kiểm tra hàng trong kho còn đủ số lượng đáp ứng hay không,  (chỉ kiểm tra ở font-end)
  cập nhật số lượng còn lại trong kho sau khi tạo hóa đơn. (đã làm)
	+ Xóa/tìm kiếm hóa đơn (đã làm)

*Chức năng nâng cao:
--------------------
  + Đăng nhập, đăng xuất (đã làm)
  + Quản lý, tạo mới, thông tin người dùng 
      (Đang gặp 2 issue: 1> get roles của user đang bị stackoverflow -> @JsonIgnore vào Set<Role> thì không bị lỗi nhưng không
                            lấy được data từ Set<Role> 
                         2> Khi gửi data json User lên server, thì không binding được trường Set<Role> -> hashcode khi add User thì
                            mặc định User đó là ROLE_MEMBER) 
   + khóa người dùng (kết hợp với chỉnh sửa user)
   + đổi mật khẩu (đã làm)
   + Kết xuất chi tiết hóa đơn ở định dạng PDF (đã làm)

------------------------------------------------------
II. Yêu cầu kỹ thuật:
------------------------------------------------------
- Sử dụng spring framework: 
  + spring mvc 
  + spring data jpa (1 số chức năng có sử dụng @Query)
  + spring security (Chưa sử dụng OAuth2 jwt vì đang vướng 1 số lỗi -> chỉ phân quyền, chưa xác thực)
  + spring boot 
  => Chưa hiểu rõ chức năng nào sử dụng api, chức năng nào sử dụng view resolver.
    
- Các tìm kiếm, phân trang dữ liệu tại server side (Chưa phân trang)
- Có validate dữ liệu đầu vào, alert, notification đầy đủ (Có validate 1 số trường)
- Có khả năng sắp xếp danh sách hóa đơn theo ngày tạo, 
    tổng giá, số lượng mặt hàng trong hóa đơn (Vì chưa phân trang được nên em chưa kết hợp được sort )
- Sử dụng thư viện Jasper Report cho kết xuất file pdf khi tìm kiếm hóa đơn (đã sử dụng)

----------------------------------------------------------
* Run project:
- 2 account mặc định được add sẵn: 
    + Admin acc: admin@gmail.com | pass: 123456
    + Member acc: member@gmail.com | pass: 123456
- Phân quyền: 
- Member: Product Management, Order (Thêm hóa đơn), Bill Management (Xem thông tin, xóa hóa đơn)
- Admin: Statistical (thống kê số lượng bán được và xuất report pdf), User Management, và những những năng của Member


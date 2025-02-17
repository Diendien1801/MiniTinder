Cấu trúc thư mục dự án Android (Kotlin + Jetpack Compose + MVC)
📂 common          # Chứa các thành phần tái sử dụng trong toàn bộ ứng dụng
│── 📂 components  # Các Composable UI chung (Button, TextField, Dialog, ...)
│── 📂 fragments   # Các Fragment tái sử dụng (LoadingFragment, ErrorFragment, ...)
│
📂 data            # Xử lý dữ liệu (Model, Repository, Service)
│── 📂 models      # Định nghĩa các data class (User, Product, Order, ...) chung cho toàn dự án
│── 📂 repositories # Repository để xử lý truy vấn dữ liệu (API, Database, ...)
│── 📂 services    # Xử lý gọi API hoặc giao tiếp với hệ thống bên ngoài. (Upload file lên firebase).
│
📂 features        # Chia các tính năng theo mô hình MVC
│── 📂 feature1    
│   │── 📂 controller # Điều khiển luồng dữ liệu và logic
│   │── 📂 model      # Định nghĩa data class liên quan đến feature này
│   │── 📂 view       # Giao diện Jetpack Compose của tính năng
│
│── 📂 feature2    
│   │── 📂 controller
│   │── 📂 model
│   │── 📂 view
│
📂 ui.theme        # Chứa các định nghĩa về giao diện (Màu sắc, Typography, Theme)
│
📂 utils           # Chứa các tiện ích dùng chung. Khi code thấy còn thiếu gì thì bổ sung
│── 📂 constants   # Chứa các hằng số (API_URL, TIMEOUT, ...)
│── 📂 validators  # Các hàm kiểm tra dữ liệu đầu vào (validate email, password, ...)

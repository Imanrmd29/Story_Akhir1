# Aplikasi Story App

# Halaman Autentikasi

Menampilkan halaman login untuk masuk ke dalam aplikasi.

- Email (R.id.ed_login_email)

- Password (R.id.ed_login_password)

Membuat halaman register untuk mendaftarkan diri dalam aplikasi.

- Nama (R.id.ed_register_name)

- Email (R.id.ed_register_email)

- Password (R.id.ed_register_password)

Password wajib disembunyikan.

Membuat Custom View berupa EditText pada halaman login atau register.

Menyimpan data sesi dan token di preferences. Data sesi digunakan untuk mengatur alur aplikasi dengan spesifikasi seperti berikut.

- Jika sudah login langsung masuk ke halaman utama.

- Jika belum maka akan masuk ke halaman login. 

Terdapat fitur untuk logout (R.id.action_logout) pada halaman utama dengan :

- Ketika tombol logout ditekan, informasi token, dan sesi akan dihapus.

# Daftar Cerita (List Story)

Menampilkan daftar cerita dari API yang disediakan.

- Nama user (R.id.tv_item_name)

-  Foto  (R.id.iv_item_photo)

Muncul halaman detail ketika salah satu item cerita ditekan.

- Nama user (R.id.tv_detail_name)

- Foto (R.id.iv_detail_photo)

- Deskripsi (R.id.tv_detail_description)


# Tambah Cerita

Membuat halaman untuk menambah cerita baru yang dapat diakses dari halaman daftar cerita.

- File foto

- Deskripsi cerita (R.id.ed_add_description)

Berikut dalam menambahkan cerita baru:

- Terdapat tombol (R.id.button_add) untuk upload data ke server. 

- Setelah tombol tersebut diklik dan proses upload berhasil, maka akan kembali ke halaman daftar cerita. 

- Data cerita terbaru harus muncul di paling atas.

# Menampilkan Animasi

Menampilkan animasi pada aplikasi dengan menggunakan Lottie Animation

# Menampilkan Maps

Menampilkan satu halaman baru berisi peta yang menampilkan semua cerita yang memiliki lokasi dengan benar. Bisa berupa marker maupun icon berupa gambar. Data story yang memiliki lokasi latitude dan longitude dapat diambil melalui parameter location dari database.

# Paging List

Menampilkan list story dengan menggunakan Paging 3 dengan benar.

# Membuat Testing

Menerapkan unit test pada fungsi di dalam ViewModel yang mengambil list data Paging.

# Membuat skenario hasil testing yang sudah di buat di ViewModel

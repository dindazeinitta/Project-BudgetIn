/**
 * Berisi fungsi-fungsi yang digunakan bersama di seluruh halaman.
 */

// Global helper untuk memformat angka menjadi format mata uang Rupiah.
function formatRupiah(number) {
  if (typeof number !== 'number') {
    return "Rp 0";
  }
  return new Intl.NumberFormat("id-ID", {
    style: "currency",
    currency: "IDR",
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  }).format(number);
}

// Logika untuk menampilkan modal konfirmasi logout.
function openLogoutModal() {
  const modal = document.getElementById("logoutModal");
  if (modal) {
    modal.classList.remove("hidden");
  }
}

// Logika untuk menyembunyikan modal konfirmasi logout.
function closeLogoutModal() {
  const modal = document.getElementById("logoutModal");
  if (modal) {
    modal.classList.add("hidden");
  }
}

// Logika untuk melakukan logout.
async function confirmLogout() {
  try {
    const response = await fetch('http://127.0.0.1:8080/api/auth/logout', {
      method: 'POST',
      credentials: 'include' // Penting untuk mengirim cookie sesi
    });

    // Terlepas dari respons server, kita tetap membersihkan sisi klien dan mengarahkan ulang.
    // Ini memastikan pengguna keluar dari aplikasi bahkan jika ada masalah jaringan.
    if (!response.ok) {
      console.warn('Server logout failed, but proceeding with client-side logout.');
    }

  } catch (error) {
    console.error('Error during logout request:', error);
  } finally {
    // Selalu jalankan pembersihan dan pengalihan
    window.location.href = "signin.html";
  }
}

// Fungsi untuk mengaktifkan atau menonaktifkan dark mode.
function toggleDarkMode(isEnabled) {
  if (isEnabled) {
    document.documentElement.classList.add('dark');
  } else {
    document.documentElement.classList.remove('dark');
  }
}

// Fungsi untuk memuat preferensi dark mode dari localStorage.
function loadDarkModePreference() {
  const isDarkMode = localStorage.getItem('darkMode') === 'true';
  toggleDarkMode(isDarkMode);
  // Pastikan checkbox di settings mencerminkan preferensi yang disimpan
  const darkModeCheckbox = document.querySelector('input[type="checkbox"][class="sr-only peer"]');
  if (darkModeCheckbox) {
    darkModeCheckbox.checked = isDarkMode;
  }
}

// Fungsi untuk menyimpan preferensi dark mode ke localStorage.
function saveDarkModePreference(isEnabled) {
  localStorage.setItem('darkMode', isEnabled);
}

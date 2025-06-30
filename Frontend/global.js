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
    const response = await fetch('/api/auth/logout', {
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
    localStorage.removeItem('userEmail');
    window.location.href = "signin.html";
  }
}
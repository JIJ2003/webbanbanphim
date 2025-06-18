<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>KeyCraft - Premium Mechanical Keyboards</title>
  <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
  <style>
    .hero-section {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      color: white;
      padding: 100px 0;
    }
    .product-card { transition: transform 0.3s; }
    .product-card:hover { transform: translateY(-5px); }
    .navbar-brand { font-weight: bold; font-size: 1.5rem; }
  </style>
</head>
<body>
  <!-- Navigation -->
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
      <a class="navbar-brand" href="/"><i class="fas fa-keyboard"></i> KeyCraft</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav me-auto">
          <li class="nav-item"><a class="nav-link active" href="/">Home</a></li>
          <li class="nav-item"><a class="nav-link" href="/products">Products</a></li>
          <c:if test="${not empty currentUser and currentUser.role eq 'ADMIN'}">
            <li class="nav-item"><a class="nav-link" href="/admin">Admin Panel</a></li>
          </c:if>
          <c:if test="${not empty currentUser}">
            <li class="nav-item">
              <a class="nav-link" href="/cart">
                <i class="fas fa-shopping-cart"></i>
                Cart
                <span id="cart-count" class="badge bg-warning text-dark">
                  ${cartItemCount}
                </span>
              </a>
            </li>
          </c:if>
        </ul>
        <ul class="navbar-nav">
          <c:choose>
            <c:when test="${not empty currentUser}">
              <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                  <i class="fas fa-user"></i>
                  <c:out value="${currentUser.firstName}" /> <c:out value="${currentUser.lastName}" />
                </a>
                <ul class="dropdown-menu">
                  <li><a class="dropdown-item" href="/auth/logout">Logout</a></li>
                </ul>
              </li>
            </c:when>
            <c:otherwise>
              <li class="nav-item"><a class="nav-link" href="/login">Login</a></li>
              <li class="nav-item"><a class="nav-link" href="/signup">Sign Up</a></li>
            </c:otherwise>
          </c:choose>
        </ul>
      </div>
    </div>
  </nav>

  <!-- Hero Section -->
  <section class="hero-section text-center">
    <div class="container">
      <h1 class="display-4 mb-4">Premium Mechanical Keyboards</h1>
      <p class="lead mb-4">Discover the perfect typing experience with our curated collection.</p>
      <a href="/products" class="btn btn-light btn-lg">
        <i class="fas fa-shopping-cart"></i> Shop Now
      </a>
    </div>
  </section>

  <!-- Featured Products -->
  <section class="py-5">
    <div class="container">
      <h2 class="text-center mb-5">Featured Products</h2>
      <div class="row">
        <c:forEach items="${featuredProducts}" var="product">
          <div class="col-md-4 mb-4">
            <div class="card product-card h-100">
              <img src="${product.imageUrl}" class="card-img-top"
                   alt="${product.name}" style="height:200px;object-fit:cover;">
              <div class="card-body d-flex flex-column">
                <h5 class="card-title">${product.name}</h5>
                <p class="card-text flex-grow-1">${product.description}</p>
                <div class="mt-auto">
                  <div class="d-flex justify-content-between align-items-center">
                    <span class="h5 text-primary">$${product.price}</span>
                    <small class="text-muted">${product.brand}</small>
                  </div>
                  <button class="btn btn-primary w-100 mt-2"
                          onclick="addToCart(${product.id}, 1)">
                    <i class="fas fa-cart-plus"></i> Add to Cart
                  </button>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </section>

  <!-- Features Section -->
  <section class="py-5 bg-light">
    <div class="container">
      <div class="row text-center">
        <div class="col-md-4 mb-4"><i class="fas fa-shipping-fast fa-3x text-primary mb-3"></i>
          <h5>Fast Shipping</h5><p>Free shipping on orders over $100</p>
        </div>
        <div class="col-md-4 mb-4"><i class="fas fa-tools fa-3x text-primary mb-3"></i>
          <h5>Custom Builds</h5><p>Professional assembly service</p>
        </div>
        <div class="col-md-4 mb-4"><i class="fas fa-headset fa-3x text-primary mb-3"></i>
          <h5>Expert Support</h5><p>Help from enthusiasts</p>
        </div>
      </div>
    </div>
  </section>

  <!-- Footer -->
  <footer class="bg-dark text-white py-4">
    <div class="container text-center">&copy; 2025 KeyCraft</div>
  </footer>

  <script src="/webjars/jquery/jquery.min.js"></script>
  <script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script>
    // Gửi add-> nhận { success, message } rồi refresh count
    function addToCart(productId, quantity) {
      fetch('/cart/add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ productId, quantity })
      })
      .then(r => r.json())
      .then(json => {
        if (json.success) {
          showToast(json.message, 'success');
          // cập nhật badge ngay:
          refreshCartCount();
        } else {
          showToast(json.message, 'error');
        }
      })
      .catch(() => showToast('Error adding to cart','error'));
    }

    // Lấy count mới từ server và hiển thị
    function refreshCartCount() {
      fetch('/cart/count')
        .then(r=>r.json())
        .then(j=>{
          const cnt = j.cartItemCount||0;
          const el = document.getElementById('cart-count');
          if (el) {
            el.textContent = cnt;
            el.style.display = cnt>0?'inline-block':'none';
          }
        })
        .catch(()=>{});
    }

    function showToast(msg,type) {
      const toastEl = document.createElement('div');
      toastEl.className =
        'toast align-items-center text-white ' +
        (type==='success'?'bg-success':'bg-danger') +
        ' border-0';
      toastEl.role = 'alert';
      toastEl.innerHTML = `
        <div class="toast-body">${msg}</div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
      `;
      document.body.append(toastEl);
      new bootstrap.Toast(toastEl, { delay:2000 }).show();
    }
  </script>
</body>
</html>

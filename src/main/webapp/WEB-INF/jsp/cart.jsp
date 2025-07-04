<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Shopping Cart - KeyCraft</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <a class="navbar-brand" href="/"><i class="fas fa-keyboard"></i> KeyCraft</a>
    <div class="navbar-nav ms-auto">
      <c:choose>
        <c:when test="${user != null}">
          <a class="nav-link" href="/products">Products</a>
          <a class="nav-link active" href="/cart">
            <i class="fas fa-shopping-cart"></i> Cart
            <span class="badge bg-primary" id="cart-count">${cartItemCount}</span>
          </a>
          <a class="nav-link" href="/orders">Orders</a>
          <a class="nav-link" href="/logout">Logout</a>
        </c:when>
        <c:otherwise>
          <a class="nav-link" href="/auth/login">Login</a>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</nav>

<div class="container mt-4">
  <h2><i class="fas fa-shopping-cart"></i> Shopping Cart</h2>

  <c:if test="${empty cartItems}">
    <div class="text-center py-5">
      <i class="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
      <h4>Your cart is empty</h4>
      <p class="text-muted">Add some keyboards to get started!</p>
      <a href="/products" class="btn btn-primary">Browse Products</a>
    </div>
  </c:if>

  <c:if test="${not empty cartItems}">
    <div class="row">
      <div class="col-md-8">
        <c:forEach var="item" items="${cartItems}">
          <div class="card mb-3" data-cart-item-id="${item.id}">
            <div class="card-body">
              <div class="row align-items-center">
                <div class="col-md-2">
                  <img src="${item.product.imageUrl}" alt="${item.product.name}" class="img-fluid rounded">
                </div>
                <div class="col-md-4">
                  <h5 class="card-title">
                    <a href="/product/${item.product.id}" class="text-decoration-none">${item.product.name}</a>
                  </h5>
                  <p class="text-muted small">${item.product.brand} - ${item.product.category}</p>
                </div>
                <div class="col-md-2">
                  <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="$" />
                </div>
                <div class="col-md-2">
                  <div class="input-group">
                    <button class="btn btn-outline-secondary"
                            type="button"
                            onclick="changeQuantity(this, -1)"
                            data-cart-item-id="${item.id}"
                            ${item.quantity <= 1 ? "disabled" : ""}>-</button>

                    <input type="number"
                           id="input-${item.id}"
                           class="form-control text-center cart-quantity"
                           value="${item.quantity}"
                           min="1"
                           max="10"
                           data-product-price="${item.product.price}"
                           onchange="changeQuantity(this, 0)">

                    <button class="btn btn-outline-secondary"
                            type="button"
                            onclick="changeQuantity(this, 1)"
                            data-cart-item-id="${item.id}"
                            ${item.quantity >= 10 ? "disabled" : ""}>+</button>
                  </div>
                </div>
                <div class="col-md-1">
                  <strong id="subtotal-${item.id}">
                    $<span>${item.subtotal}</span>
                  </strong>
                </div>
                <div class="col-md-1">
                  <button class="btn btn-danger btn-sm" onclick="removeFromCart('${item.id}')">
                    <i class="fas fa-trash"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>

      <div class="col-md-4">
        <div class="card">
          <div class="card-header">
            <h5>Order Summary</h5>
          </div>
          <div class="card-body">
            <div class="d-flex justify-content-between mb-2">
              <span>Items (<span id="total-items">${cartItemCount}</span>)</span>
              <span id="cart-total">
                <fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$" />
              </span>
            </div>
            <div class="d-flex justify-content-between mb-2">
              <span>Shipping:</span><span>Free</span>
            </div>
            <hr>
            <div class="d-flex justify-content-between fw-bold">
              <span>Total:</span>
              <span id="final-total">
                <fmt:formatNumber value="${cartTotal}" type="currency" currencySymbol="$" />
              </span>
            </div>
            <button class="btn btn-primary w-100 mt-3" onclick="proceedToCheckout()">Proceed to Checkout</button>
            <button class="btn btn-outline-secondary w-100 mt-2" onclick="clearCart()">Clear Cart</button>
          </div>
        </div>
      </div>
    </div>
  </c:if>
</div>

<!-- Toast -->
<div class="toast-container position-fixed bottom-0 end-0 p-3">
  <div id="notification-toast" class="toast" role="alert">
    <div class="toast-header">
      <strong class="me-auto">KeyCraft</strong>
      <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
    </div>
    <div class="toast-body" id="toast-message"></div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
  function changeQuantity(el, delta) {
    // el: nút + hoặc -, delta: +1 / -1 / 0 khi onchange
    let cartItemId = el.getAttribute('data-cart-item-id');
    if (!cartItemId) {
      // nếu onchange (delta=0), data nằm trên input:
      cartItemId = el.id.replace('input-','');
      el = document.getElementById('input-' + cartItemId);
      delta = 0;
    }
    const input = delta === 0 ? el : el.closest('.input-group').querySelector('input.cart-quantity');
    let qty = parseInt(input.value) || 1;
    if (delta !== 0) {
      qty = Math.max(1, Math.min(10, qty + delta));
      input.value = qty;
    }
    updateQuantity(cartItemId, qty);
  }

  function updateQuantity(cartItemId, quantity) {
    fetch('/cart/update', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ cartItemId, quantity })
    })
    .then(res => res.json())
    .then(data => {
      showNotification(data.message, data.success ? 'success' : 'error');
      if (data.success) updateCartDisplay(data, cartItemId, quantity);
    });
  }

  function updateCartDisplay(data, cartItemId, quantity) {
    // badge & summary count
    document.getElementById('cart-count').textContent = data.cartItemCount;
    document.getElementById('total-items').textContent = data.cartItemCount;

    // subtotal của item
    const span = document.querySelector('#subtotal-' + cartItemId + ' span');
    const input = document.getElementById('input-' + cartItemId);
    if (span && input) {
      const price = parseFloat(input.dataset.productPrice);
      span.textContent = (price * quantity).toFixed(2);
    }

    // tổng tiền
    if (data.cartTotal !== undefined) {
      document.getElementById('cart-total').textContent = '$' + data.cartTotal.toFixed(2);
      document.getElementById('final-total').textContent = '$' + data.cartTotal.toFixed(2);
    }
  }

  function removeFromCart(cartItemId) {
    fetch('/cart/remove', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ cartItemId })
    })
    .then(res => res.json())
    .then(data => {
      showNotification(data.message, data.success ? 'success' : 'error');
      if (data.success) {
        document.querySelector('[data-cart-item-id="'+cartItemId+'"]').remove();
        updateCartDisplay(data);
        if (data.cartItemCount === 0) location.reload();
      }
    });
  }

  function clearCart() {
    if (!confirm('Are you sure?')) return;
    fetch('/cart/clear', {method:'POST'})
      .then(res => res.json())
      .then(data => { if (data.success) location.reload(); });
  }

  function proceedToCheckout() {
    window.location.href = '/checkout';
  }

  function showNotification(msg, type) {
    const toast = document.getElementById('notification-toast'),
          body  = document.getElementById('toast-message');
    body.textContent = msg;
    toast.className = 'toast ' + (type==='success'?'bg-success text-white':'bg-danger text-white');
    new bootstrap.Toast(toast).show();
  }
</script>
</body>
</html>

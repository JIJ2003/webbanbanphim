<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders - KeyCraft</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="/">
                <i class="fas fa-keyboard"></i> KeyCraft
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/products">Products</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/cart">Cart</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user"></i> ${currentUser.firstName} ${currentUser.lastName}
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item active" href="/orders">My Orders</a></li>
                            <li><a class="dropdown-item" href="/auth/logout">Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Orders Content -->
    <div class="container my-4">
        <h2><i class="fas fa-shopping-bag"></i> My Orders</h2>
        
        <c:choose>
            <c:when test="${empty orders}">
                <div class="text-center py-5">
                    <i class="fas fa-shopping-bag fa-3x text-muted mb-3"></i>
                    <h4>No orders yet</h4>
                    <p class="text-muted">You haven't placed any orders yet. Start shopping to see your orders here.</p>
                    <a href="/products" class="btn btn-primary">Start Shopping</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row">
                    <c:forEach items="${orders}" var="order">
                        <div class="col-12 mb-4">
                            <div class="card">
                                <div class="card-header">
                                    <div class="row align-items-center">
                                        <div class="col-md-6">
                                            <h6 class="mb-0">Order #${order.id}</h6>
                                            <small class="text-muted">
<fmt:formatDate value="${order.createdAtDate}" pattern="MMM dd, yyyy 'at' HH:mm" />
                                            </small>
                                        </div>
                                        <div class="col-md-3 text-center">
                                            <c:choose>
                                                <c:when test="${order.status == 'PENDING'}">
                                                    <span class="badge bg-warning">Pending</span>
                                                </c:when>
                                                <c:when test="${order.status == 'CONFIRMED'}">
                                                    <span class="badge bg-success">Confirmed</span>
                                                </c:when>
                                                <c:when test="${order.status == 'SHIPPED'}">
                                                    <span class="badge bg-info">Shipped</span>
                                                </c:when>
                                                <c:when test="${order.status == 'DELIVERED'}">
                                                    <span class="badge bg-primary">Delivered</span>
                                                </c:when>
                                                <c:when test="${order.status == 'CANCELLED'}">
                                                    <span class="badge bg-danger">Cancelled</span>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                        <div class="col-md-3 text-end">
                                            <strong>$${order.totalAmount}</strong>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-body">
                                    <div class="row">
                                        <div class="col-md-8">
                                            <h6>Items (${order.orderItems.size()})</h6>
                                            <div class="row">
                                                <c:forEach items="${order.orderItems}" var="item" varStatus="loop">
                                                    <c:if test="${loop.index < 3}">
                                                        <div class="col-md-4 mb-2">
                                                            <div class="d-flex align-items-center">
                                                                <img src="${item.product.imageUrl}" alt="${item.product.name}" 
                                                                     class="me-2 rounded" style="width: 40px; height: 40px; object-fit: cover;">
                                                                <div>
                                                                    <small class="fw-bold">${item.product.name}</small>
                                                                    <small class="text-muted d-block">Qty: ${item.quantity}</small>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </c:forEach>
                                                <c:if test="${order.orderItems.size() > 3}">
                                                    <div class="col-md-4">
                                                        <small class="text-muted">+${order.orderItems.size() - 3} more items</small>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                        <div class="col-md-4 text-end">
                                            <a href="/orders/${order.id}" class="btn btn-outline-primary">
                                                <i class="fas fa-eye"></i> View Details
                                            </a>
                                            <c:if test="${order.status == 'PENDING' || order.status == 'CONFIRMED'}">
        <button class="btn btn-outline-danger ms-2 cancel-order-btn" data-id="${order.id}">
            <i class="fas fa-times"></i> Cancel Order
        </button>
    </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script>
  $(function () {
    $('.cancel-order-btn').on('click', function () {
      const id = $(this).data('id');
      if (confirm("Are you sure you want to cancel order #" + id + "?")) {
        $.ajax({
          url: '/api/orders/' + id,
          type: 'PUT',
          contentType: 'application/json',
          data: JSON.stringify({ status: 'CANCELLED' }),
          success: () => location.reload(),
          error: () => alert("Failed to cancel order.")
        });
      }
    });
  });
</script>
    
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.keycraft.model.Order.OrderStatus" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">  
  <title>Dashboard - KeyCraft</title>
  <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
<!-- Navbar -->
<nav class="navbar navbar-dark bg-dark">
  <div class="container">
    <a class="navbar-brand" href="/"><i class="fas fa-keyboard"></i> KeyCraft Dashboard</a>
    <div class="navbar-nav ms-auto d-flex flex-row gap-3">
      <a class="nav-link text-white" href="/"><i class="fas fa-store"></i> Back to Store</a>
      <a class="nav-link text-white" href="/auth/logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
    </div>
  </div>
</nav>

<div class="container my-4">
  <h1><i class="fas fa-cogs"></i> Admin Dashboard
    <small class="text-muted">Welcome, <c:out value="${currentUser.firstName}" />!</small>
  </h1>

  <!-- Nav Tabs -->
  <ul class="nav nav-tabs mt-4" id="adminTabs" role="tablist">
    <li class="nav-item"><a class="nav-link active" data-bs-toggle="tab" href="#users">Users</a></li>
    <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#products">Products</a></li>
    <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#orders">Orders</a></li>
    <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#services">Services</a></li>
  </ul>

  <div class="tab-content mt-4">
<!-- Users -->
<div class="tab-pane fade show active" id="users">
<div class="d-flex justify-content-between align-items-center mb-3">
  <h4><i class="fas fa-users"></i> User Management</h4>
  <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addUserModal">
    <i class="fas fa-plus"></i> Add User
  </button>
</div>
  
  <div class="table-responsive">
    <table class="table table-striped">
      <thead>
        <tr>
          <th>ID</th><th>Email</th><th>Full Name</th><th>Role</th><th>Registered At</th><th>Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${users}" var="u">
          <tr>
            <td>${u.id}</td>
            <td>${u.email}</td>
            <td>${u.firstName} ${u.lastName}</td>
<td>
  <c:choose>
    <c:when test="${u.role == 'ADMIN'}">
      <span class="badge bg-success">Admin</span>
    </c:when>
    <c:otherwise>
      <span class="badge bg-secondary">Customer</span>
    </c:otherwise>
  </c:choose>
</td>
            <td>${u.createdAt}</td>
            <td>
              <button class="btn btn-sm btn-outline-primary" onclick="editUser(${u.id})">
                <i class="fas fa-edit"></i>
              </button>
              <button class="btn btn-sm btn-outline-danger" onclick="deleteUser(${u.id})">
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>


    <!-- Products -->
    <div class="tab-pane fade" id="products">
      <div class="d-flex justify-content-between align-items-center mb-3">
        <h4><i class="fas fa-boxes"></i> Product Management</h4>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addProductModal">
          <i class="fas fa-plus"></i> Add Product
        </button>
      </div>
      <div class="table-responsive">
        <table class="table table-striped">
          <thead>
            <tr>
              <th>Image</th><th>Name</th><th>Brand</th><th>Price</th><th>Stock</th><th>Featured</th><th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${products}" var="p">
              <tr>
                <td><img src="${p.imageUrl}" alt="${p.name}" style="width:50px;height:50px;object-fit:cover;" class="rounded"></td>
                <td>${p.name}</td>
                <td>${p.brand}</td>
                <td>$${p.price}</td>
                <td>
<c:choose>
  <c:when test="${p.stock > 10}">
    <span class="badge bg-success">${p.stock}</span>
  </c:when>
  <c:when test="${p.stock > 0}">
    <span class="badge bg-warning">${p.stock}</span>
  </c:when>
  <c:otherwise>
    <span class="badge bg-danger">${p.stock}</span>
  </c:otherwise>
</c:choose>

                </td>
                <td>
                  <c:choose>
                    <c:when test="${p.featured}">
                      <span class="badge bg-primary">Featured</span>
                    </c:when>
                    <c:otherwise>
                      <span class="badge bg-secondary">Regular</span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <button class="btn btn-sm btn-outline-primary" onclick="editProduct(${p.id})">
                    <i class="fas fa-edit"></i>
                  </button>
                  <button class="btn btn-sm btn-outline-danger" onclick="deleteProduct(${p.id})">
                    <i class="fas fa-trash"></i>
                  </button>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Orders -->
    
    <!-- Orders Tab Content -->
    <div class="tab-pane fade" id="orders">
      <table class="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>User</th>
            <th>Created At</th>
            <th>Status</th>
            <th>Total</th>
            <th>Tracking Code</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${orders}" var="o">
            <tr>
              <td>#${o.id}</td>
              <td>${o.user.firstName} ${o.user.lastName}</td>
              <td>${o.createdAt}</td>
              <td>
                <select class="form-select form-select-sm order-status" data-id="${o.id}">
  <c:forEach var="s" items="${['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED']}">
    <c:choose>
      <c:when test="${o.status.name() == s}">
        <option value="${s}" selected>${s}</option>
      </c:when>
      <c:otherwise>
        <option value="${s}">${s}</option>
      </c:otherwise>
    </c:choose>
  </c:forEach>
</select>

              </td>
              <td>$${o.totalAmount}</td>
              <td>
                <c:choose>
                  <c:when test="${o.status != 'SHIPPED'}">
                    <input type="text" class="form-control form-control-sm tracking-input" 
                           data-id="${o.id}" value="${o.trackingCode}" 
                           placeholder="Tracking code" disabled/>
                  </c:when>
                  <c:otherwise>
                    <input type="text" class="form-control form-control-sm tracking-input" 
                           data-id="${o.id}" value="${o.trackingCode}" 
                           placeholder="Tracking code"/>
                  </c:otherwise>
                </c:choose>
              </td>
              <td>
                <button class="btn btn-sm btn-success save-order" data-id="${o.id}">
                  <i class="fas fa-save"></i> Save
                </button>
                <a href="/orders/${o.id}" class="btn btn-sm btn-outline-secondary">
                  <i class="fas fa-eye"></i>
                </a>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>

    <!-- Services -->
    <div class="tab-pane fade" id="services">
      <h4><i class="fas fa-tools"></i> Service Bookings</h4>
      <div class="alert alert-info">Coming soon: Service booking management</div>
    </div>
  </div>
</div>

<!-- Add Product Modal -->
<div class="modal fade" id="addProductModal" tabindex="-1">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <form id="addProductForm">
        <div class="modal-header">
          <h5 class="modal-title">Add New Product</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <div class="row mb-3">
            <div class="col"><label class="form-label">Name</label>
              <input type="text" class="form-control" id="name" required></div>
            <div class="col"><label class="form-label">Brand</label>
              <input type="text" class="form-control" id="brand" required></div>
          </div>
          <div class="mb-3">
            <label class="form-label">Description</label>
            <textarea class="form-control" id="description" rows="2" required></textarea>
          </div>
          <div class="row mb-3">
            <div class="col"><label class="form-label">Price</label>
              <input type="number" step="0.01" class="form-control" id="price" required></div>
            <div class="col"><label class="form-label">Stock</label>
              <input type="number" class="form-control" id="stock" required></div>
            <div class="col"><label class="form-label">Category</label>
              <select class="form-select" id="category" required>
                <option value="mechanical-keyboards">Mechanical Keyboards</option>
                <option value="switches">Switches</option>
                <option value="keycaps">Keycaps</option>
                <option value="accessories">Accessories</option>
              </select>
            </div>
          </div>
          <div class="row mb-3">
            <div class="col"><label class="form-label">Switch Type</label>
              <select class="form-select" id="switchType">
                <option value="">None</option>
                <option value="linear">Linear</option>
                <option value="tactile">Tactile</option>
                <option value="clicky">Clicky</option>
              </select>
            </div>
            <div class="col"><label class="form-label">Layout</label>
              <input type="text" class="form-control" id="layout" placeholder="e.g. 60%, TKL"></div>
          </div>
          <div class="mb-3">
            <label class="form-label">Image URL</label>
            <input type="url" class="form-control" id="imageUrl" required>
          </div>
          <div class="form-check mb-0">
            <input class="form-check-input" type="checkbox" id="featured">
            <label class="form-check-label">Featured</label>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
          <button class="btn btn-primary" type="submit">Add Product</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Edit Product Modal -->
<div class="modal fade" id="editProductModal" tabindex="-1">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <form id="editProductForm">
        <div class="modal-header">
          <h5 class="modal-title">Edit Product</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <input type="hidden" id="editProductId">
          <div class="row mb-3">
            <div class="col"><label class="form-label">Name</label>
              <input type="text" class="form-control" id="editName" required></div>
            <div class="col"><label class="form-label">Brand</label>
              <input type="text" class="form-control" id="editBrand" required></div>
          </div>
          <div class="mb-3">
            <label class="form-label">Description</label>
            <textarea class="form-control" id="editDescription" rows="2" required></textarea>
          </div>
          <div class="row mb-3">
            <div class="col"><label class="form-label">Price</label>
              <input type="number" step="0.01" class="form-control" id="editPrice" required></div>
            <div class="col"><label class="form-label">Stock</label>
              <input type="number" class="form-control" id="editStock" required></div>
            <div class="col"><label class="form-label">Category</label>
              <select class="form-select" id="editCategory" required>
                <option value="mechanical-keyboards">Mechanical Keyboards</option>
                <option value="switches">Switches</option>
                <option value="keycaps">Keycaps</option>
                <option value="accessories">Accessories</option>
              </select>
            </div>
          </div>
          <div class="row mb-3">
            <div class="col"><label class="form-label">Switch Type</label>
              <select class="form-select" id="editSwitchType">
                <option value="">None</option>
                <option value="linear">Linear</option>
                <option value="tactile">Tactile</option>
                <option value="clicky">Clicky</option>
              </select>
            </div>
            <div class="col"><label class="form-label">Layout</label>
              <input type="text" class="form-control" id="editLayout"></div>
          </div>
          <div class="mb-3">
            <label class="form-label">Image URL</label>
            <input type="url" class="form-control" id="editImageUrl" required>
          </div>
          <div class="form-check mb-0">
            <input class="form-check-input" type="checkbox" id="editFeatured">
            <label class="form-check-label">Featured</label>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
          <button class="btn btn-primary" type="submit">Save Changes</button>
        </div>
      </form>
    </div>
  </div>
</div>
<!-- Edit User Modal -->
<div class="modal fade" id="editUserModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id="editUserForm">
        <div class="modal-header">
          <h5 class="modal-title">Edit User</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <input type="hidden" id="editUserId">
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" class="form-control" id="editUserEmail" required>
          </div>
          <div class="mb-3">
            <label class="form-label">First Name</label>
            <input type="text" class="form-control" id="editUserFirstName" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Last Name</label>
            <input type="text" class="form-control" id="editUserLastName" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Role</label>
            <select class="form-select" id="editUserRole" required>
              <option value="CUSTOMER">Customer</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <div class="mb-3">
  <label class="form-label">Password</label>
  <input type="password" class="form-control" id="editUserPassword" required>
</div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
          <button class="btn btn-primary" type="submit">Save Changes</button>
        </div>
      </form>
    </div>
  </div>
</div>
<!-- Add User Modal -->
<div class="modal fade" id="addUserModal" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id="addUserForm">
        <div class="modal-header">
          <h5 class="modal-title">Add New User</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>
        <div class="modal-body">
          <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" class="form-control" id="addUserEmail" required>
          </div>
          <div class="mb-3">
            <label class="form-label">First Name</label>
            <input type="text" class="form-control" id="addUserFirstName" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Last Name</label>
            <input type="text" class="form-control" id="addUserLastName" required>
          </div>
          <div class="mb-3">
            <label class="form-label">Role</label>
            <select class="form-select" id="addUserRole" required>
              <option value="CUSTOMER">Customer</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-label">Password</label>
            <input type="password" class="form-control" id="addUserPassword" required>
            <small class="form-text text-muted">Leave blank to keep current password</small>
        
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
          <button class="btn btn-primary" type="submit">Add User</button>
        </div>
      </form>
    </div>
  </div>
</div>


<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
<script>
$(function(){
  // Edit
  window.editProduct = function(id){
    $.getJSON('/api/products/'+id, function(p){
      $('#editProductId').val(p.id);
      $('#editName').val(p.name);
      $('#editBrand').val(p.brand);
      $('#editDescription').val(p.description);
      $('#editPrice').val(p.price);
      $('#editStock').val(p.stock);
      $('#editCategory').val(p.category);
      $('#editSwitchType').val(p.switchType||'');
      $('#editLayout').val(p.layout||'');
      $('#editImageUrl').val(p.imageUrl);
      $('#editFeatured').prop('checked', p.featured);
      new bootstrap.Modal($('#editProductModal')[0]).show();
    });
  };

  // Delete
  window.deleteProduct = function(id){
    if(confirm('Xóa product #'+id+'?')){
      $.ajax({url:'/api/products/'+id, type:'DELETE', success:()=>location.reload()});
    }
  };

  // Add
  $('#addProductForm').submit(function(e){
    e.preventDefault();
    var data = {
      name:$('#name').val(), brand:$('#brand').val(),
      description:$('#description').val(), price:parseFloat($('#price').val()),
      stock:parseInt($('#stock').val()), category:$('#category').val(),
      switchType:$('#switchType').val()||null, layout:$('#layout').val()||null,
      imageUrl:$('#imageUrl').val(), featured:$('#featured').is(':checked')
    };
    $.ajax({
      url:'/api/products', type:'POST', contentType:'application/json',
      data: JSON.stringify(data),
      success: ()=>location.reload()
    });
  });

  // Save edit
  $('#editProductForm').submit(function(e){
    e.preventDefault();
    var id = $('#editProductId').val();
    var data = {
      name:$('#editName').val(), brand:$('#editBrand').val(),
      description:$('#editDescription').val(), price:parseFloat($('#editPrice').val()),
      stock:parseInt($('#editStock').val()), category:$('#editCategory').val(),
      switchType:$('#editSwitchType').val()||null, layout:$('#editLayout').val()||null,
      imageUrl:$('#editImageUrl').val(), featured:$('#editFeatured').is(':checked')
    };
    $.ajax({
      url:'/api/products/'+id, type:'PUT', contentType:'application/json',
      data: JSON.stringify(data),
      success: ()=>location.reload()
    });
  });
});
// user

// Add new user
$('#addUserForm').submit(function (e) {
  e.preventDefault();

  const data = {
    email: $('#addUserEmail').val(),
    firstName: $('#addUserFirstName').val(),
    lastName: $('#addUserLastName').val(),
    role: $('#addUserRole').val(),
    password: $('#addUserPassword').val()
  };

  $.ajax({
    url: '/api/users',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(data),
    success: () => {
      $('#addUserModal').modal('hide');
      location.reload();
    },
    error: function (xhr) {
      if (xhr.status === 409) {
        alert("Email already exists. Please use another email.");
      } else {
        alert("Failed to add user. Please check your input.");
      }
    }
  });
});


// Edit user
window.editUser = function(id) {
  $.getJSON('/api/users/' + id, function(u) {
    $('#editUserId').val(u.id);
    $('#editUserEmail').val(u.email);
    $('#editUserFirstName').val(u.firstName);
    $('#editUserLastName').val(u.lastName);
    $('#editUserRole').val(u.role);
    new bootstrap.Modal($('#editUserModal')[0]).show();
  });
};

// Save edited user
$('#editUserForm').submit(function(e) {
  e.preventDefault();
  const id = $('#editUserId').val();
  const password = $('#editUserPassword').val();

  const data = {
    email: $('#editUserEmail').val(),
    firstName: $('#editUserFirstName').val(),
    lastName: $('#editUserLastName').val(),
    role: $('#editUserRole').val()
  };

  // Chỉ thêm password nếu người dùng nhập
  if (password.trim() !== '') {
    data.password = password;
  }

  $.ajax({
    url: '/api/users/' + id,
    type: 'PUT',
    contentType: 'application/json',
    data: JSON.stringify(data),
    success: () => location.reload()
  });
});


// Delete user
window.deleteUser = function(id) {
  if (confirm('Xoá user #' + id + '?')) {
    $.ajax({
      url: '/api/users/' + id,
      type: 'DELETE',
      success: () => location.reload()
    });
  }
};
//Save active tab
// Lưu tab đang active vào localStorage
$('a[data-bs-toggle="tab"]').on('shown.bs.tab', function (e) {
  localStorage.setItem('activeAdminTab', $(e.target).attr('href'));
});

// Khi trang load, kiểm tra và mở lại tab đã lưu
$(function () {
  const activeTab = localStorage.getItem('activeAdminTab');
  if (activeTab) {
    $('#adminTabs a[href="' + activeTab + '"]').tab('show');
  }
});

//order
$(function(){
  $('.order-status').on('change', function () {
    const id = $(this).data('id');
    const newStatus = $(this).val();
    const trackingInput = $('.tracking-input[data-id="' + id + '"]');
    if (newStatus === 'SHIPPING') {
      trackingInput.prop('disabled', false);
    } else {
      trackingInput.prop('disabled', true).val('');
    }
  });

  $('.save-order').on('click', function () {
    const id = $(this).data('id');
    const status = $('.order-status[data-id="' + id + '"]').val();
    const trackingCode = $('.tracking-input[data-id="' + id + '"]').val();

    $.ajax({
      url: '/api/orders/' + id,
      type: 'PUT',
      contentType: 'application/json',
      data: JSON.stringify({ status, trackingCode }),
      success: () => location.reload(),
      error: () => alert("Failed to update order.")
    });
  });
});


</script>
</body>
</html>

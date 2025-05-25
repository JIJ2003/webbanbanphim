import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { useCart } from "@/hooks/useCart";
import { useToast } from "@/hooks/use-toast";
import type { Product } from "@shared/schema";

interface ProductCardProps {
  product: Product;
}

export default function ProductCard({ product }: ProductCardProps) {
  const { addToCart, isAddingToCart } = useCart();
  const { toast } = useToast();

  const handleAddToCart = () => {
    addToCart({ productId: product.id, quantity: 1 });
    toast({
      title: "Added to Cart",
      description: `${product.name} has been added to your cart.`,
    });
  };

  return (
    <Card className="bg-white hover:shadow-md transition-shadow group cursor-pointer">
      <div className="relative overflow-hidden">
        <img 
          src={product.imageUrl || "https://images.unsplash.com/photo-1587829741301-dc798b83add3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=400&h=300"} 
          alt={product.name}
          className="w-full h-48 object-cover rounded-t-lg group-hover:scale-105 transition-transform"
        />
        {product.featured && (
          <Badge className="absolute top-2 right-2 bg-accent">Featured</Badge>
        )}
        {product.stock < 5 && product.stock > 0 && (
          <Badge variant="destructive" className="absolute top-2 left-2">
            Low Stock
          </Badge>
        )}
        {product.stock === 0 && (
          <Badge variant="secondary" className="absolute top-2 left-2">
            Out of Stock
          </Badge>
        )}
      </div>
      <CardContent className="p-4">
        <h4 className="font-semibold mb-2">{product.name}</h4>
        <p className="text-gray-600 text-sm mb-2 line-clamp-2">
          {product.description}
        </p>
        <div className="flex items-center gap-2 mb-2">
          {product.brand && (
            <Badge variant="outline" className="text-xs">
              {product.brand}
            </Badge>
          )}
          {product.switchType && (
            <Badge variant="outline" className="text-xs">
              {product.switchType}
            </Badge>
          )}
        </div>
        <div className="flex justify-between items-center">
          <span className="text-lg font-bold text-primary">
            ${parseFloat(product.price).toFixed(2)}
          </span>
          <Button 
            size="sm"
            className="bg-accent hover:bg-yellow-500 text-white"
            onClick={handleAddToCart}
            disabled={isAddingToCart || product.stock === 0}
          >
            {product.stock === 0 ? "Out of Stock" : "Add to Cart"}
          </Button>
        </div>
      </CardContent>
    </Card>
  );
}

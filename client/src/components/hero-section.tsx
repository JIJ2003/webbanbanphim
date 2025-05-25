import { useQuery } from "@tanstack/react-query";
import { Button } from "@/components/ui/button";
import type { Product } from "@shared/schema";

export default function HeroSection() {
  const { data: featuredProducts = [] } = useQuery<Product[]>({
    queryKey: ["/api/products/featured"],
  });

  return (
    <section className="bg-primary text-white py-16">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid md:grid-cols-2 gap-12 items-center">
          <div>
            <h2 className="text-4xl md:text-5xl font-bold mb-6">
              Premium Mechanical Keyboards
            </h2>
            <p className="text-xl text-gray-300 mb-8">
              Discover the perfect typing experience with our curated collection of mechanical keyboards, 
              custom builds, and professional services.
            </p>
            <div className="flex flex-col sm:flex-row gap-4">
              <Button size="lg" className="bg-accent hover:bg-yellow-500 text-white">
                Shop Now
              </Button>
              <Button 
                size="lg" 
                variant="outline" 
                className="border-gray-300 hover:bg-white hover:text-primary text-white"
              >
                Custom Build
              </Button>
            </div>
          </div>
          <div className="relative">
            <img 
              src="https://images.unsplash.com/photo-1541140532154-b024d705b90a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&h=600" 
              alt="Premium mechanical keyboard with RGB lighting" 
              className="rounded-xl shadow-2xl w-full h-auto"
            />
          </div>
        </div>
      </div>
    </section>
  );
}

import {Component, OnInit} from '@angular/core';
import {ProductsService} from "../../products.service";
import {Product} from "../../../model/Product";
import {ImageService} from "../../image.service";

@Component({
  selector: 'app-banner',
  templateUrl: './banner.component.html',
  styleUrls: ['./banner.component.css']
})
export class BannerComponent implements OnInit {
  products: Product[] = []

  constructor(private productService: ProductsService, private imageService: ImageService) {

  }

  ngOnInit(): void {
    this.productService.getBannerProducts().subscribe(data => this.products = data);
    (function($) {
      'use strict';



    })(jQuery);

  }

  transformData(data: Product): Product {
    return this.imageService.transformData(data);
  }
  prevSlide(): void {
    $('.banner-slider-3').slick('slickPrev');
  }

  nextSlide(): void {
    $('.banner-slider-3').slick('slickNext');
  }
}

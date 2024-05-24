import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-custom-captcha',
  templateUrl: './custom-captcha.component.html',
  styleUrls: ['./custom-captcha.component.css']
})
export class CustomCaptchaComponent {
  @Output() captchaResolved = new EventEmitter<boolean>();

  question: string = '';
  answer: string = '';
  userAnswer: string = '';

  ngOnInit() {
    this.generateQuestion();
  }

  generateQuestion() {
    // Example: Simple arithmetic operation
    const num1 = Math.floor(Math.random() * 10);
    const num2 = Math.floor(Math.random() * 10);
    this.question = `What is ${num1} + ${num2}?`;
    this.answer = (num1 + num2).toString();
  }

  validateAnswer() {
    if (this.userAnswer === this.answer) {
      this.captchaResolved.emit(true);
    } else {
      alert('Incorrect answer, please try again.');
      this.generateQuestion();
      this.userAnswer = '';
    }
  }
}

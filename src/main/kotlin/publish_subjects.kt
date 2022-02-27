import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.ReplaySubject

/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

fun main(args: Array<String>) {

    exampleOf("PublishingSubject") {
        val publishSubject = PublishSubject.create<Int>()
        val subscriptionOne = publishSubject.subscribe { int ->
            println(int)
        }
        publishSubject.onNext(1)
        publishSubject.onNext(2)
        val subscribeTwo = publishSubject.subscribe { int ->
            printWithLabel("2)", int)
        }
        publishSubject.onNext(3)
        subscriptionOne.dispose()
        publishSubject.onNext(4)
        publishSubject.onComplete()
        publishSubject.onNext(5)
        subscribeTwo.dispose()
        val subscriptionThree = publishSubject.subscribeBy(
            onNext = { println(it) },
            onComplete = { println("Complete") }
        )
        publishSubject.onNext(6)
    }

    exampleOf("BehaviourSubjects") {
        val subscriptions = CompositeDisposable()
        val behaviorSubject = BehaviorSubject.createDefault("Initial value")
        behaviorSubject.onNext("X")
        val subscriptionOne = behaviorSubject.subscribeBy(
            onNext = { printWithLabel("1)", it) },
            onError = { printWithLabel("1)", it) }
        )
        behaviorSubject.onError(RuntimeException("Error!"))
        subscriptions.add(behaviorSubject.subscribeBy(
            onNext = { printWithLabel("2)", it) },
            onError = { printWithLabel("2)", it) }
        ))
    }

    exampleOf("Behaviour State") {
        val subscriptions = CompositeDisposable()
        val behaviourSubject = BehaviorSubject.createDefault(0)
        println(behaviourSubject.value)

        subscriptions.add(behaviourSubject.subscribeBy {
            printWithLabel("1)", it)
        })
        behaviourSubject.onNext(1)
        println(behaviourSubject.value)
        subscriptions.dispose()
    }

    exampleOf("ReplaySUbject") {
        val subscriptions = CompositeDisposable()
        val replaySubject = ReplaySubject.createWithSize<String>(3)
        replaySubject.onNext("1")
        replaySubject.onNext("2")
        replaySubject.onNext("3")

        subscriptions.add(replaySubject.subscribeBy(
            onNext = { printWithLabel("1)", it) },
            onError = { printWithLabel("1)", it) }
        ))

        subscriptions.add(replaySubject.subscribeBy(
            onNext = { printWithLabel("2)", it) },
            onError = { printWithLabel("2)", it) }
        ))

        replaySubject.onNext("4")
        replaySubject.onError(RuntimeException("Error!"))

        subscriptions.add(replaySubject.subscribeBy(
            onNext = { printWithLabel("3)", it) },
            onError = { printWithLabel("3)", it) }
        ))
    }

}
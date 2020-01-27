package edu.rosehulman.improse.myapplication

import androidx.fragment.app.Fragment

abstract class HasChildFragment: Fragment() {

   abstract fun switchToChildFragment(pos:Int);

}
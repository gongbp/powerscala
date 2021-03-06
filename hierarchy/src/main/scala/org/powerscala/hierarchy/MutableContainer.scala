/*
 * Copyright (c) 2011 PowerScala
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *  Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 *  Neither the name of 'PowerScala' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.powerscala.hierarchy

import annotation.tailrec

/**
 * MutableContainer as the name suggests is a mutable implementation of Container.
 *
 * @author Matt Hicks <mhicks@powerscala.org>
 */
trait MutableContainer[T] extends AbstractMutableContainer[T] {
  override val contents = new VisibleContents

  /**
   * Represents the children of this container.
   */
  class VisibleContents extends Seq[T] {
    def iterator = buffer.iterator

    /**
     * Lookup a child by index.
     */
    def apply(index: Int) = buffer(index)

    /**
     * The number of children in this container.
     */
    def length = buffer.length

    /**
     * Inserts a child into this container and assigns this container as the parent if the child is
     * of type Element.
     */
    def +=(child: T) = addChild(child)

    /**
     * Inserts the supplied children into this container beginning at the index provided and assigns this container as
     * the parent if the child is of type Element.
     */
    def insert(index: Int, children: T*) = insertChildren(index, children: _*)

    /**
     * Adds the supplied children immediately after the supplied child in this container.
     *
     * @param existingChild the child currently in this container to add after
     * @param childrenToAdd the children to add
     */
    def addAfter(existingChild: T, childrenToAdd: T*) = {
      val index = indexOf(existingChild)
      if (index == -1) {
        throw new IndexOutOfBoundsException("Unable to find %s in this container.".format(existingChild))
      }
      insertChildren(index + 1, childrenToAdd: _*)
    }

    /**
     * Removes the supplied child from this container and nullifies parent if the child is of type
     * Element.
     */
    def -=(child: T) = removeChild(child)

    /**
     * Removes all children from this container.
     */
    def clear() = removeAll()

    /**
     * Adds all the supplied children to this container.
     */
    @tailrec
    final def addAll(children: T*): Unit = {
      if (children.nonEmpty) {
        this += children.head
        addAll(children.tail: _*)
      }
    }

    /**
     * Removes all existing children and adds the supplied ones.
     */
    def replaceWith(children: T*) = {
      clear()
      addAll(children: _*)
    }
  }
}
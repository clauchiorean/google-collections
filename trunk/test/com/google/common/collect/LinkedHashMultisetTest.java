/*
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import static com.google.common.collect.helpers.MoreAsserts.assertContentsInOrder;
import java.util.Iterator;

/**
 * Unit test for {@code LinkedHashMultiset}.
 *
 * @author kevinb
 */
public class LinkedHashMultisetTest extends AbstractMultisetTest {
  @Override
  protected <E> Multiset<E> create() {
    return new LinkedHashMultiset<E>();
  }

  // 70+ test cases are inherited...

  public void testIteratorBashing() throws Exception {
    ms = createSample();
    IteratorTester tester = new IteratorTester(9) {
      @Override protected Iterator<?> newReferenceIterator() {
        return Lists.newArrayList(ms).iterator();
      }
      @Override protected Iterator<?> newTargetIterator() {
        return createSample().iterator();
      }
    };
    tester.test();
  }

  public void testElementSetIteratorBashing() throws Exception {
    IteratorTester tester = new IteratorTester(7) {
      @Override protected Iterator<?> newReferenceIterator() {
        return Lists.newArrayList("a", "c", "b").iterator();
      }
      @Override protected Iterator<?> newTargetIterator() {
        Multiset<String> multiset = create();
        multiset.add("a", 3);
        multiset.add("c", 1);
        multiset.add("b", 2);
        return multiset.elementSet().iterator();
      }
    };
    tester.test();
  }

  public void testToString() {
    ms.add("a", 3);
    ms.add("c", 1);
    ms.add("b", 2);

    assertEquals("[a x 3, c, b x 2]", ms.toString());
  }

  public void testLosesPlaceInLine() throws Exception {
    ms.add("a");
    ms.add("b", 2);
    ms.add("c");
    assertContentsInOrder(ms.elementSet(), "a", "b", "c");
    ms.remove("b");
    assertContentsInOrder(ms.elementSet(), "a", "b", "c");
    ms.add("b");
    assertContentsInOrder(ms.elementSet(), "a", "b", "c");
    ms.remove("b", 2);
    ms.add("b");
    assertContentsInOrder(ms.elementSet(), "a", "c", "b");
  }

  @SuppressWarnings("unchecked")
  public void testClone() {
    ms.add("a");
    ms.add("b", 2);
    ms.add("c");
    Multiset<String> clone = ((LinkedHashMultiset<String>) ms).clone();
    assertContentsInOrder(ms, "a", "b", "b", "c");
    assertContentsInOrder(clone, "a", "b", "b", "c");
    assertTrue(ms.equals(clone));
    assertTrue(clone.equals(ms));
    ms.add("foo");
    assertContentsInOrder(ms, "a", "b", "b", "c", "foo");
    assertContentsInOrder(clone, "a", "b", "b", "c");
    assertFalse(ms.equals(clone));
    assertFalse(clone.equals(ms));
  }
}
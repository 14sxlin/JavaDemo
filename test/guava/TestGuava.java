package guava;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

public class TestGuava {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Test
	public void testOptional() {
		Integer a = new Integer(10);
		Integer b = null;
		Optional<Integer> aOption = Optional.of(a);
		Optional<Integer> bOption = Optional.fromNullable(b);

		assertEquals(new Integer(10), sum(aOption, bOption));

		expectedEx.expect(NullPointerException.class);
		sum(a, b);

	}

	private Integer sum(Integer a, Integer b) {
		return a + b;
	}

	private Integer sum(Optional<Integer> a, Optional<Integer> b) {
		if (a.isPresent()) {
			System.out.println("a is present");
		} else {
			System.out.println("a is not present");
		}
		if (b.isPresent()) {
			System.out.println("b is present");
		} else {
			System.out.println("b is not present");
		}
		Integer int1 = a.or(new Integer(0));
		Integer int2 = b.or(new Integer(0));
		return int1 + int2;
	}

	@Test
	public void testPrecondition() {
		expectedEx.expect(IllegalArgumentException.class);
		sqrt(-0.3);
		concat(null, "test");
		get(5);
	}

	private double sqrt(double val) {
		Preconditions.checkArgument(val >= 0, "don't sqrt negetive num ");
		return Math.sqrt(val);

	}

	private String concat(String a, String b) {
		Preconditions.checkNotNull(a);
		Preconditions.checkNotNull(b);
		return a + b;
	}

	private int get(int index) {
		int a[] = { 1, 2, 3, 4, 5, 6 };
		// element 不包含最后一个 position包含最后一个
		Preconditions.checkElementIndex(index, a.length);
		return a[index];
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testOrdering() {
		List<Integer> numbers = new ArrayList<Integer>();
		numbers.add(new Integer(5));
		numbers.add(new Integer(2));
		numbers.add(new Integer(15));
		numbers.add(new Integer(51));
		numbers.add(new Integer(53));
		numbers.add(new Integer(35));
		numbers.add(new Integer(45));
		numbers.add(new Integer(32));
		numbers.add(new Integer(43));
		numbers.add(new Integer(16));

		Ordering ordering = Ordering.natural();
		System.out.println("Input List: ");
		System.out.println(numbers);

		Collections.sort(numbers, ordering);
		System.out.println("Sorted List: ");
		System.out.println(numbers);

		System.out.println("======================");
		System.out.println("List is sorted: " + ordering.isOrdered(numbers));
		System.out.println("Minimum: " + ordering.min(numbers));
		System.out.println("Maximum: " + ordering.max(numbers));

		Collections.sort(numbers, ordering.reverse());
		System.out.println("Reverse: " + numbers);

		numbers.add(null);
		System.out.println("Null added to Sorted List: ");
		System.out.println(numbers);

		Collections.sort(numbers, ordering.nullsFirst());
		System.out.println("Null first Sorted List: ");
		System.out.println(numbers);
		System.out.println("======================");

		List<String> names = new ArrayList<String>();
		names.add("Ram");
		names.add("Shyam");
		names.add("Mohan");
		names.add("Sohan");
		names.add("Ramesh");
		names.add("Suresh");
		names.add("Naresh");
		names.add("Mahesh");
		names.add(null);
		names.add("Vikas");
		names.add("Deepak");

		System.out.println("Another List: ");
		System.out.println(names);

		Collections.sort(names, ordering.nullsFirst().reverse());
		System.out.println("Null first then reverse sorted list: ");
		System.out.println(names);
	}

}

package root.adt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BitSetTest.class, CollectorCharArrayTest.class, GraphDirectedTest.class, ListArrayLongSortedTest.class, ListArrayLongTest.class,
		ListArraySortedTest.class, ListArrayTest.class, ListExtractableTest.class, ListHashedTest.class, ListImmutableTest.class,
		ListLazyLoadTest.class, ListLinkedTest.class, MapBidirectionalTest.class, MapBuilder.class, MapExtractableTest.class, MapHashedTest.class,
		MapImmutableTest.class, MapMultiValueTest.class, SetImmutableTest.class, QueueLinkedTest.class, SetHashedTest.class, StackLinkedTest.class,
		SetMultiKeyTest.class, StackArrayTest.class })
class AbstractDataTypeTestSuite {
}

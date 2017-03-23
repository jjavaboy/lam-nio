package lam.pool.support;
/**
* <p>
* pooled object state
* </p>
* @author linanmiao
* @date 2017年3月23日
* @version 1.0
* @see org.apache.commons.pool2.PooledObjectState
*/
public enum SPooledObjectState {
	
	IDLE,
	
	ALLOCATED,
	
	/**
     * In the queue, currently being tested for possible eviction.
     */
	EVICTION,
	
	/**
     * Not in the queue, currently being tested for possible eviction. An
     * attempt to borrow the object was made while being tested which removed it
     * from the queue. It should be returned to the head of the queue once
     * eviction testing completes.
     * TODO: Consider allocating object and ignoring the result of the eviction
     *       test.
     */
	EVICTION_RETURN_TO_HEAD,
	
	VALIDATION,
	
	/**
     * Not in queue, currently being validated. The object was borrowed while
     * being validated and since testOnBorrow was configured, it was removed
     * from the queue and pre-allocated. It should be allocated once validation
     * completes.
     */
	VALIDATION_PREALLOCATED,
	
	/**
     * Not in queue, currently being validated. An attempt to borrow the object
     * was made while previously being tested for eviction which removed it from
     * the queue. It should be returned to the head of the queue once validation
     * completes.
     */
	VALIDATION_RETURN_TO_HEAD,
	
	INVALID,
	
	ABANDONED,
	
	RETURNING

}

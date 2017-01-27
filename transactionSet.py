"""
Transaction set model
"""

class TrasactionSet(object):
    """TransactionSet contaiting the actual transaction and its support, confidencevalues"""
    def __init__(self):
        self.transaction = []
        self.support = 0
